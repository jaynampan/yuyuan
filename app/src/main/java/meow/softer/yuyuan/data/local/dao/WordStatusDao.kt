package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import meow.softer.yuyuan.data.local.entiity.WordStatus
import java.time.ZonedDateTime

@Dao
interface WordStatusDao {
    // order: Query, Update, Insert, Delete

    @Query("SELECT * FROM word_status")
    fun getAll(): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE id = :id AND user_id = :userId")
    fun getById(id: Int, userId: Int): WordStatus

    @Query("SELECT * FROM word_status WHERE word_id = :wordId AND user_id = :userId")
    fun getByWordId(wordId: Int, userId: Int): WordStatus

    @Query("SELECT * FROM word_status WHERE is_learnt = 1 AND user_id = :userId")
    fun getLearnt(userId: Int): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_starred = 1 AND user_id = :userId")
    fun getStarred(userId: Int): List<WordStatus>

    // TODO("Haven't tested yet")
    @Query("SELECT * FROM word_status WHERE DATE(time_learned) = DATE(:date) AND user_id = :userId ")
    fun getLearntByDate(date: ZonedDateTime, userId: Int): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE time_learned BETWEEN :start AND :end AND user_id = :userId")
    fun getLearntByInterval(
        start: ZonedDateTime,
        end: ZonedDateTime,
        userId: Int
    ): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_learnt = 0 AND user_id = :userId")
    fun getAllNew(userId: Int): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_learnt = 0 AND user_id = :userId LIMIT :limit ")
    fun getNewWithLimit(limit: Int, userId: Int): List<WordStatus>

    @Query("SELECT word_id FROM word_status WHERE is_learnt = 0 AND user_id = :userId LIMIT :limit")
    fun getRandomNewWordIds(limit: Int, userId: Int): List<Int>

    @Transaction
    @Query("UPDATE word_status SET is_starred = NOT is_starred WHERE word_id = :wordId AND user_id = :userId")
    fun toggleStarred(wordId: Int, userId: Int)

    @Transaction
    @Query("UPDATE word_status SET is_starred = NOT is_starred WHERE id = :id")
    fun toggleStarredByStatusId(id: Int)

    @Transaction
    @Query("UPDATE word_status SET is_learnt = 1, time_learned = :dateTime  WHERE word_id = :wordId AND user_id = :userId")
    fun setLearntByWordId(wordId: Int, userId: Int, dateTime: ZonedDateTime)

    @Transaction
    @Query("UPDATE word_status SET is_learnt = 1, time_learned = :dateTime WHERE id = :id")
    fun setLearntByStatusId(id: Int, dateTime: ZonedDateTime)

    @Insert
    fun insertAll(vararg wordStatus: WordStatus)

    @Delete
    fun delete(wordStatus: WordStatus)

    @Query("""
        SELECT word_id 
        FROM word_status 
        JOIN words 
        WHERE  user_id = :userId AND words.book_id = :bookId
        ORDER BY RANDOM()
        LIMIT :limit
        """)
    fun getRandomNewWordIdsByBook(bookId: Int, limit: Int, userId: Int): List<Int>
}
