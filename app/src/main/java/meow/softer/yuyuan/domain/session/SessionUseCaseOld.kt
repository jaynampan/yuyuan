package meow.softer.yuyuan.domain.session


import android.content.res.AssetManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import meow.softer.yuyuan.utils.debug
import meow.softer.yuyuan.utils.isToday
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlan
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.dailyplan.DailyPlanRepository
import meow.softer.yuyuan.data.repository.dailyplanword.DailyPlanWordRepository
import meow.softer.yuyuan.data.repository.runInBackground
import meow.softer.yuyuan.data.repository.sentence.SentenceRepository
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.data.repository.media.SoundRepositoryOld
import meow.softer.yuyuan.data.repository.user.UserRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.repository.wordstatus.WordStatusRepository
import meow.softer.yuyuan.data.successOr
import javax.inject.Inject
import javax.inject.Singleton

private val MyTag = SessionUseCaseOld::class.simpleName

data class Action(
    val word: Word?,
    val sentence: Sentence?,
    val type: ActionType
)

enum class ActionType {
    Ok, Error, End
}

/**
 * Singleton TODO:Test
 */
@Singleton
class SessionUseCaseOld @Inject constructor(
    private val assetManager: AssetManager,
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    private val wordRepository: WordRepository,
    private val sentenceRepository: SentenceRepository,
    private val soundRepositoryOld: SoundRepositoryOld,
    private val dailyPlayRepository: DailyPlanRepository,
    private val wordStatusRepository: WordStatusRepository,
    private val dailyPlanWordRepository: DailyPlanWordRepository
) {

    private val _isPlanPrepared = MutableStateFlow(false)
    val isPlanPrepared: StateFlow<Boolean> = _isPlanPrepared

    private var wordIdList: List<Int> = listOf()
    private var currentWordIdx = 0

    suspend fun preparePlan(): Result<DailyPlan> {
        return runInBackground {
            // get current user's id
            val currentUserId =1


            // check if a plan for today exists
            val plans = dailyPlayRepository.getPlansByUserId(currentUserId).successOr(null)
            var todayPlan: DailyPlan? = null
            if (!plans.isNullOrEmpty()) {
                val plan = plans.find { it.date.isToday() }
                if (plan != null) {
                    debug("reused plan", MyTag)
                    todayPlan = plan
                }
            }
            // if not, clear previous plans and  create a new plan
            if (todayPlan == null) {
                dailyPlayRepository.clearAll()
                todayPlan = generateNewPlan(currentUserId)
                debug("prepare: gened new plan ", MyTag)
            }
            debug("dailyplan = $todayPlan", MyTag)
            val result = dailyPlanWordRepository.getAllByPlanId(todayPlan.id)
            when (result) {
                is Result.Success -> debug("result is success: ${result.data}", MyTag)
                is Result.Error -> debug("result failed: " + result.exception.toString(), MyTag)
            }
            wordIdList = dailyPlanWordRepository.getAllByPlanId(todayPlan.id).successOr(null)
                ?.map { it.wordId } ?: listOf()
            _isPlanPrepared.value = true
            debug("is prepared!", MyTag)
            debug("word id list = $wordIdList", MyTag)
            todayPlan
        }
    }

    private suspend fun generateNewPlan(currentUserId: Int): DailyPlan {
        // create a daily plan
        val newPlan = dailyPlayRepository.create().successOr(null)!!
        debug("newPlan = $newPlan", this::class.simpleName)
        // get current book, words to learn per day
        val currentBookId = userRepository.getCurrentBookId(currentUserId).successOr(-1)
        debug("bookId: $currentBookId", MyTag)
        val wordsPerDay = 10
        debug("words per day: $wordsPerDay", MyTag)
        // select N words from word status of a book that are not learnt
        if (currentBookId > 0 && wordsPerDay > 0) {
            val newWordIds = wordStatusRepository.getRandomNewWordIds(
                bookId = currentBookId,
                limit = wordsPerDay
            ).successOr(null)!!

            // insert into daily plan words
            debug(newWordIds.toString(), MyTag)
            dailyPlanWordRepository.addToPlan(newPlan.id, newWordIds)
            debug("genNewPlan: gened new plan", MyTag)
            return newPlan
        } else {
            throw IllegalArgumentException("getRandomNewWordIds received invalid params.")
        }

    }

    suspend fun getNextAction(): Action {
        return if (currentWordIdx < wordIdList.size) {
            val wordId = currentWordIdx++
            val word = wordRepository.getWordById(wordIdList[wordId]).successOr(null)
            val sentence = sentenceRepository.getSentenceByWordId(wordId = wordId).successOr(null)
            if (word != null && sentence != null) {
                Action(
                    word = word,
                    sentence = sentence,
                    type = ActionType.Ok
                )
            } else {
                Action(
                    word = word,
                    sentence = sentence,
                    type = ActionType.Error
                )
            }

        } else {
            Action(
                null,
                null,
                type = ActionType.End
            )
        }
    }

    suspend fun mockWord(): Word? {
        return wordRepository.getWordById(120).successOr(null)
    }

    suspend fun mockSentence(): Sentence? {
        return sentenceRepository.getSentenceById(120).successOr(null)
    }

    suspend fun playWordSound(source: String) {
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }

    suspend fun playSentenceSound(source: String) {
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }

    fun toggleStarred() {
        TODO("Not yet implemented")
    }

}