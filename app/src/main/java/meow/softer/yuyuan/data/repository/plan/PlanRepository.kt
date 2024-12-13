package meow.softer.yuyuan.data.repository.plan

import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.repository.book.BookRepository
import meow.softer.yuyuan.data.repository.setting.SettingRepository
import meow.softer.yuyuan.data.repository.user.UserRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.successOr
import meow.softer.yuyuan.ui.home.BookInfo
import meow.softer.yuyuan.ui.home.PlanInfo
import javax.inject.Inject

/**
 * Responsible for overall Plan Info
 * should use repositories to access data
 */
class PlanRepository @Inject constructor(
    private val settings: SettingRepository,
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val wordRepository: WordRepository
) : IPlanRepository {
    private lateinit var currentBook: BookInfo
    private val wordsPerDay = mutableIntStateOf(15)
    private val toLearnCount = mutableIntStateOf(15)
    private val toReviewCount = mutableIntStateOf(30)

    override suspend fun getCurrentPlan(): Result<PlanInfo> {
        currentBook = getCurrentBookInfo() ?: BookInfo(
            bookName = "Error",
            learntWords = 0,
            totalWords = 0
        )
        return withContext(Dispatchers.IO) {
            val plan = PlanInfo(
                currentBook = currentBook,
                daysLeft = calcDaysLeft(
                    wordsPerDay = wordsPerDay.intValue,
                    book = currentBook
                ),
                toLearnAmount = toLearnCount.intValue,
                toReviewAmount = toReviewCount.intValue
            )
            Result.Success(plan)
        }
    }

    override suspend fun updateLearnt(increment: Int) {
        currentBook =
            currentBook.copy(learntWords = currentBook.learntWords + increment)
    }

    /**
     * Calculate how many days left to finish the book, round up
     */
    private fun calcDaysLeft(wordsPerDay: Int, book: BookInfo): Int {
        val remainingWords = book.totalWords - book.learntWords
        return ((remainingWords + wordsPerDay - 1) / wordsPerDay)
    }

    private suspend fun getCurrentBookInfo(): BookInfo? {
        return withContext(Dispatchers.IO) {
            val currentUserId = settings.getCurrentUserId()
            val currentBookId =
                userRepository.getCurrentBookId(userId = currentUserId).successOr(-1)
            if (currentBookId == -1) return@withContext null

            val wordCount = wordRepository.getWordCountByBookId(currentBookId).successOr(-1)
            val currentBook = bookRepository.getBookById(currentBookId).successOr(null)

            return@withContext if (wordCount > 0 && currentBook != null) {
                BookInfo(
                    bookName = currentBook.bookTitle,
                    learntWords = 0,
                    totalWords = wordCount.toInt()
                )
            } else {
                null
            }
        }
    }
}