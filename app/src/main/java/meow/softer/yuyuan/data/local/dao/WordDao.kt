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

    @Query("SELECT COUNT(character) FROM words where book_id = :bookId")
    fun countByBookId(bookId: Int): Int

    @Query("SELECT COUNT(character) FROM words")
    fun countWords(): Int

}