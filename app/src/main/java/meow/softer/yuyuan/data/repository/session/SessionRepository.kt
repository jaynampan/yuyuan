package meow.softer.yuyuan.data.repository.session

import android.content.res.AssetManager
import meow.softer.yuyuan.common.utils.isToday
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlan
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.dailyplan.DailyPlanRepository
import meow.softer.yuyuan.data.repository.music.MusicRepository
import meow.softer.yuyuan.data.repository.runInBackground
import meow.softer.yuyuan.data.repository.sentence.SentenceRepository
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.data.repository.user.UserRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.repository.wordstatus.WordStatusRepository
import meow.softer.yuyuan.data.successOr
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val assetManager: AssetManager,
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    private val wordRepository: WordRepository,
    private val sentenceRepository: SentenceRepository,
    private val musicRepository: MusicRepository,
    private val dailyPlayRepository: DailyPlanRepository,
    private val wordStatusRepository: WordStatusRepository
) : ISessionRepository {

    override suspend fun preparePlan(): Result<Unit> {
        return runInBackground {
            // get current user's id
            val currentUserId = settingRepository.getCurrentUserId()


            // check if a plan for today exists
            val plans = dailyPlayRepository.getPlansByUserId(currentUserId).successOr(null)
            var todayPlan: DailyPlan? = null
            if (!plans.isNullOrEmpty()) {
                val plan = plans.find { it.date.isToday() }
                if (plan != null) {
                    todayPlan = plan
                }
            }
            // if not, create a new plan
            if (todayPlan == null) {
                todayPlan = generateNewPlan(currentUserId, currentUserId)
            }
            Result.Success(Unit)
        }
    }

    override suspend fun generateNewPlan(currentUserId: Int, currentUserId1: Int): DailyPlan {
        // create a daily plan
        val newPlan = dailyPlayRepository.create().successOr(null)
        // get current book, words to learn per day
        val currentBookId = userRepository.getCurrentBookId(currentUserId).successOr(-1)
        val wordsPerDay = settingRepository.getWordsPerDay()
        // select N words from word status of a book that are not learnt
        if (currentBookId > 0 && wordsPerDay > 0) {
            val newWordIds = wordStatusRepository.getRandomNewWordIds(
                bookId = currentBookId,
                limit = wordsPerDay
            )
        }else{
            throw IllegalArgumentException("getRandomNewWordIds received invalid params.")
        }

        // insert into daily plan words
        TODO()

    }

    override suspend fun mockWord(): Word? {
        return wordRepository.getWordById(120).successOr(null)
    }

    override suspend fun mockSentence(): Sentence? {
        return sentenceRepository.getSentenceById(120).successOr(null)
    }

    override suspend fun playWordSound(source: String) {
        musicRepository.setSource(assetManager.openFd(source))
//        musicRepository.play()
    }

    override suspend fun playSentenceSound(source: String) {
        musicRepository.setSource(assetManager.openFd(source))
//        musicRepository.play()
    }

}