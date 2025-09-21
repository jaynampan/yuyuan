package meow.softer.yuyuan.data.repository.wordstatus

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.local.entiity.WordStatus
import java.time.ZonedDateTime

/**
 * Responsible for tracking words' learning progress
 * interacting with wordStatus data
 * query and update
 * user_id defaults to 1
 *
 */


interface IWordStatusRepository {
    /**
     * Get all wordStatus records
     */
    suspend fun getAllWordStatus(): Result<List<WordStatus>>

    /**
     * Get a single word's wordStatus by wordId
     */
    suspend fun getWordStatusByWordId(
        wordId: Int
    ): Result<WordStatus>

    /**
     * Get a single wordStatus by wordStatus' id
     */
    suspend fun getWordStatusById(
        id: Int
    ): Result<WordStatus>

    /**
     * Get learnt words' wordStatus list
     */
    suspend fun getLearntList(): Result<List<WordStatus>>

    /**
     * Get starred words' wordStatus list
     */
    suspend fun getStarredList(): Result<List<WordStatus>>

    /**
     * Get learnt words' wordStatus in a day
     */
    suspend fun getLearntListByDate(
        date: ZonedDateTime
    ): Result<List<WordStatus>>

    /**
     * Get learnt words' wordStatus in a time interval
     */
    suspend fun getLearntListByInterval(
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<WordStatus>>


    /**
     * Update the word's starred status
     * By word, needs userId too
     */
    suspend fun toggleStarred(word: Word): Result<Unit>

    /**
     * Update the word's starred status
     * By wordStatus, it's enough
     */
    suspend fun toggleStarred(wordStatus: WordStatus): Result<Unit>

    /**
     * Update the word's starred status
     * By wordStatus id, it's enough
     */
    suspend fun toggleStarredByStatusId(wordStatusId: Int): Result<Unit>

    /**
     * Update the word's starred status
     * By wordId, needs userId too
     */
    suspend fun toggleStarredByWordId(wordId: Int): Result<Unit>

    /**
     * Set word to learnt with current dateTime
     * By wordId, needs userId too
     */
    suspend fun setLearntByWordId(wordId: Int): Result<Unit>

    /**
     * Set word to learnt with current dateTime
     * By word, needs userId too
     */
    suspend fun setLearnt(word: Word): Result<Unit>

    /**
     * Set word to learnt with current dateTime
     * By wordStatus, it's enough
     */
    suspend fun setLearnt(wordStatus: WordStatus): Result<Unit>

    /**
     * Set word to learnt with current dateTime
     * By wordStatus id, it's enough
     */
    suspend fun setLearntByStatusId(statusId: Int): Result<Unit>

    /**
     * Get not learnt word's status list
     * pass in limit to specify list size,
     * get all by default
     */
    suspend fun getNewWordList(
        limit: Int? = null
    ): Result<List<WordStatus>>

    /**
     * Get new words' ids by random
     * MUST pass in limit to specify list size
     *
     */
    suspend fun getRandomNewWordIds(
        limit: Int,

    ): Result<List<Int>>

    /**
     * Get a book's new words' ids by random
     */
    suspend fun getRandomNewWordIds(
        bookId: Int,
        limit: Int,
    ): Result<List<Int>>

}