package meow.softer.yuyuan.domain

import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word

/**
 * UI state for Book
 */
data class BookInfo(
    val bookId: Int,
    val bookName: String,
    val learntWords: Int,
    val totalWords: Int
)

/**
 * UI state for Daily Plan
 */
data class PlanInfo(
    val currentBook: BookInfo,
    val daysLeft: Int,
    val toLearnAmount: Int,
    val toReviewAmount: Int
)

/**
 * UI State of User's settings
 *
 * Note the speed is real speed in float format
 */
data class UserSetting(
    val currentBookId: Int,
    val currentAudioSpeed: Float
)

/**
 * UI State for Hanzi, including both word and sentence info
 */
data class HanziInfo(
    val word: Word,
    val sentence: Sentence
)