package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.repository.wordstatus.WordStatusRepository
import meow.softer.yuyuan.data.successOr
import javax.inject.Inject

class SaveProgressUseCase @Inject constructor(
    private val wordStatusRepository: WordStatusRepository,
    private val wordRepository: WordRepository
) {
    /**
     * Save the word as learnt
     * @param word the newly learnt word
     * @return the current learnt word count in the book, -1 to indicate error
     */
    suspend operator fun invoke(word: Word): Int {
        wordStatusRepository.setLearnt(word)
        val count = wordRepository.getWordLearntCountByBook(word.bookId).successOr(null)
        return count ?: -1
    }
}
