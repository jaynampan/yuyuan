package meow.softer.yuyuan.data.repository.plan

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.domain.BookInfo
import meow.softer.yuyuan.domain.PlanInfo
import javax.inject.Inject

/**
 * Implementation of PlanRepository that returns a hardcoded plan info
 * after some delay in a background thread.
 */
class MockPlanRepository @Inject constructor() : IPlanRepository {
    private val currentBook = mutableStateOf(
        BookInfo(
            bookName = "HSK1",
            learntWords = 25,
            totalWords = 300,
            bookId = 0,
        )
    )
    private val wordsPerDay = mutableIntStateOf(15)
    private val toLearnCount = mutableIntStateOf(15)
    private val toReviewCount = mutableIntStateOf(30)

    override suspend fun getCurrentPlan(): Result<PlanInfo> {
        return withContext(Dispatchers.IO) {
            val plan = PlanInfo(
                currentBook = currentBook.value,
                daysLeft = calcDaysLeft(
                    wordsPerDay = wordsPerDay.intValue,
                    book = currentBook.value
                ),
                toLearnAmount = toLearnCount.intValue,
                toReviewAmount = toReviewCount.intValue
            )
            Result.Success(plan)
        }
    }

    override suspend fun updateLearnt(increment: Int) {
        currentBook.value =
            currentBook.value.copy(learntWords = currentBook.value.learntWords + increment)
    }

    /**
     * Calculate how many days left to finish the book, round up
     */
    private fun calcDaysLeft(wordsPerDay: Int, book: BookInfo): Int {
        val remainingWords = book.totalWords - book.learntWords
        return ((remainingWords + wordsPerDay - 1) / wordsPerDay)
    }
}