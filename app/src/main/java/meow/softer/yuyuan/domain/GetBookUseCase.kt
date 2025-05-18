package meow.softer.yuyuan.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    suspend operator fun invoke(bookId: Int): BookInfo = coroutineScope {
        debug("get book invoked id = $bookId")
        // get in parallel
        val nameJob = async { bookRepository.getBookById(bookId).successOr(null)?.bookTitle ?: "" }
        val totalJob = async { wordRepository.getWordCountByBookId(bookId).successOr(1) }
        val wordLearntJob = async { wordRepository.getWordLearntCountByBook(bookId).successOr(0) }

        val name = nameJob.await()
        val wordLearnt = wordLearntJob.await()
        val total = totalJob.await()

        BookInfo(
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