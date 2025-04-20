package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.data.repository.book.BookRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.successOr
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject


class GetBookUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val wordRepository: WordRepository
) {
    /**
     * Get a book info when specified book id
     */
    suspend operator fun invoke(bookId: Int): BookInfo {
        debug("get book invoked id = $bookId")
        val name: String = bookRepository.getBookById(bookId).successOr(null)?.bookTitle ?: ""
        val total = wordRepository.getWordCountByBookId(bookId).successOr(1)
        val wordLearnt = wordRepository.getWordLearntCountByBook(bookId).successOr(0)
        return BookInfo(
            bookName = name,
            learntWords = wordLearnt,
            totalWords = total,
            bookId = bookId
        )
    }

    suspend operator fun invoke(): List<Book>? {
        return bookRepository.getAllBooks().successOr(null)
    }
}