package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.repository.sentence.SentenceRepository
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.repository.wordstatus.WordStatusRepository
import meow.softer.yuyuan.data.successOr
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

class GetHanziInfoUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
    private val wordStatusRepository: WordStatusRepository,
    private val wordRepository: WordRepository,
    private val sentenceRepository: SentenceRepository
) {
    suspend operator fun invoke(limit: Int): List<HanziInfo> {
        val bookId = settingRepository.getSettings().currentBookId
        val newestId = wordStatusRepository.getNewestLearntWordIdByBook(bookId).successOr(-1)
        // 0 for brand new book
        if (newestId < 0) {
            debug("get cache: newestId < 0")
            return emptyList()
        }
        val words = wordRepository.getByBookWithLimit(newestId, bookId, limit).successOr(null)
        val ids = words?.map { it.id }
        if (ids.isNullOrEmpty()) {
            debug("get cache: ids are null or empty")
            return emptyList()
        }
        val sentences = sentenceRepository.getSentenceByWordIds(ids).successOr(null)
        if (sentences.isNullOrEmpty() || sentences.size != words.size) {
            debug("get cache:error 3 ")
            return emptyList()
        }
        val result = words.zip(sentences) { word, sentence -> HanziInfo(word, sentence) }
        return  result
    }
}
