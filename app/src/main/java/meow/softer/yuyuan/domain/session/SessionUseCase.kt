package meow.softer.yuyuan.domain.session


import android.content.res.AssetManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.media.SoundRepositoryOld
import meow.softer.yuyuan.data.repository.sentence.SentenceRepository
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.successOr
import meow.softer.yuyuan.utils.debug
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Singleton TODO:Test
 */
@Singleton
class SessionUseCase @Inject constructor(
    private val assetManager: AssetManager,
    private val settingRepository: SettingRepository,
    private val wordRepository: WordRepository,
    private val sentenceRepository: SentenceRepository,
    private val soundRepositoryOld: SoundRepositoryOld,
) {

    private val _isPrepared = MutableStateFlow(false)
    val isPrepared: StateFlow<Boolean> = _isPrepared

    private var currentWordIdx = 0
    private var maxIdx = 0
    private var currentBook = 0
    suspend fun prepare() {
        debug("Session prepared")
        currentBook = getCurrentBook()
        currentWordIdx = getLearntWordId(currentBook)
        maxIdx = wordRepository.getWordCountByBookId(currentBook).successOr(1) - 1
        _isPrepared.value = true
    }

    suspend fun getNextAction(): Action {
        // TODO: save progress
        // settingRepository.setCurrentWordId(currentBook, currentWordIdx)
        return if (currentWordIdx <= maxIdx) {
            val wordId = currentWordIdx++
            val word = wordRepository.getWordById(wordId).successOr(null)
            val sentence = sentenceRepository.getSentenceByWordId(wordId = wordId).successOr(null)
            debug("Action: $word $sentence")
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

    fun mockWord(): Word? {
        return Word(
            id = 9999,
            character = "错误",
            pinyin = "",
            audioFile = "",
            charJsonFile = "",
            meaning = "Error!",
            bookId = 10
        )
    }

    fun mockSentence(): Sentence? {
        return Sentence(
            id = 9999,
            wordId = 9999,
            content = "程序错误",
            translation = "There's error in app!",
            pinyin = "",
            audioFile = "",
            createdAt = ZonedDateTime.now()
        )
    }

    suspend fun playWordSound(source: String) {
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }

    suspend fun playSentenceSound(source: String) {
        soundRepositoryOld.playOnce(assetManager.openFd(source))
    }

    fun getLearntWordId(book: Int): Int {
        return 1
        //TODO: fix
    }

    suspend fun getCurrentBook(): Int {
        return settingRepository.getSettings().currentBookId
    }


    suspend fun setCurrentBookId(bookId:Int){
        settingRepository.setCurrentBookId(bookId)
    }

   suspend fun setCurrentSpeed(speed: Float) {
        soundRepositoryOld.setSpeed(speed)
    }

}