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


    @Query("SELECT * FROM word_status WHERE id = :id")
    fun getById(id: Int): WordStatus

    @Query("SELECT * FROM word_status WHERE word_id = :wordId")
    fun getByWordId(wordId: Int): WordStatus

    @Query("SELECT * FROM word_status WHERE is_learnt = 1")
    fun getLearnt(): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_starred = 1")
    fun getStarred(): List<WordStatus>

    // TODO("Haven't tested yet")
    @Query("SELECT * FROM word_status WHERE DATE(time_learned) = DATE(:date)")
    fun getLearntByDate(date: ZonedDateTime): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE time_learned BETWEEN :start AND :end")
    fun getLearntByInterval(
        start: ZonedDateTime,
        end: ZonedDateTime
    ): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_learnt = 0")
    fun getAllNew(): List<WordStatus>

    @Query("SELECT * FROM word_status WHERE is_learnt = 0 LIMIT :limit ")
    fun getNewWithLimit(limit: Int): List<WordStatus>

    @Query("SELECT word_id FROM word_status WHERE is_learnt = 0 LIMIT :limit")
    fun getRandomNewWordIds(limit: Int): List<Int>

    @Transaction
    @Query("UPDATE word_status SET is_starred = NOT is_starred WHERE word_id = :wordId")
    fun toggleStarred(wordId: Int)

    @Transaction
    @Query("UPDATE word_status SET is_starred = NOT is_starred WHERE id = :id")
    fun toggleStarredByStatusId(id: Int)

    @Transaction
    @Query("UPDATE word_status SET is_learnt = 1, time_learned = :dateTime  WHERE word_id = :wordId")
    fun setLearntByWordId(wordId: Int, dateTime: ZonedDateTime)

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
        WHERE words.book_id = :bookId
        ORDER BY RANDOM()
        LIMIT :limit
        """)
    fun getRandomNewWordIdsByBook(bookId: Int, limit: Int): List<Int>

    @Query("""
        select max(w.id) from words as w 
        join word_status as s 
        on s.word_id = w.id 
        where w.book_id = :bookId and s.is_learnt = 1
    """)
    fun getNewestLearntWordIdByBook(bookId: Int):Int
}
