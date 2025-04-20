package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import meow.softer.yuyuan.data.local.entiity.Word

@Dao
interface WordDao {
    @Insert
    fun insertAll(vararg words: Word)

    @Query("SELECT * FROM words WHERE id = :id")
    fun getById(id: Int): Word

    @Delete
    fun delete(word: Word)

    @Query("SELECT * FROM words WHERE book_id = :bookId")
    fun getByBookId(bookId: Int): List<Word>

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Query("SELECT * FROM words WHERE id IN (:ids)")
    fun getByIds(ids: List<Int>): List<Word>

    @Query("SELECT COUNT(character) FROM words WHERE book_id = :bookId")
    fun countByBookId(bookId: Int): Int

    @Query("SELECT COUNT(character) FROM words")
    fun countWords(): Int

    @Query(
        "SELECT COUNT(w.id) FROM words AS w " +
                "JOIN word_status AS s " +
                "ON w.id = s.word_id " +
                "WHERE w.book_id = :bookId AND s.is_learnt = 1"
    )
    fun countWordLearntByBook(bookId: Int): Int

    @Query("select * from words where id > :startId and book_id = :bookId limit :limit")
    fun getByBookWithLimit(startId:Int,bookId: Int, limit: Int): List<Word>

}