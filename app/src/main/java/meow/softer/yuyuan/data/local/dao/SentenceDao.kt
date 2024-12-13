package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import meow.softer.yuyuan.data.local.entiity.Sentence

@Dao
interface SentenceDao {

    @Query("SELECT * FROM sentences")
    fun getAll(): List<Sentence>

    @Query("SELECT COUNT(sentence) FROM sentences")
    fun countAll(): Long

    @Query("SELECT * FROM sentences WHERE word_id = :wordId")
    fun getByWordId(wordId: Int): Sentence

    @Query("SELECT * FROM sentences WHERE word_id IN (:wordIds)")
    fun getByWordIds(wordIds: List<Int>): List<Sentence>

    @Query("SELECT * FROM sentences WHERE id = :id")
    fun getById(id: Int): Sentence

    @Query("SELECT * FROM sentences WHERE id IN (:ids)")
    fun getByIds(ids: List<Int>): List<Sentence>

    @Insert
    fun insertAll(vararg sentences: Sentence)

    @Delete
    fun delete(sentence: Sentence)
}