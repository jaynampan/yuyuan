package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import meow.softer.yuyuan.data.local.entiity.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAll(): List<Book>

    @Query("SELECT * FROM books where id = :id")
    fun getById(id: Int): Book

    @Insert
    fun insertAll(vararg books: Book)

    @Delete
    fun delete(book: Book)


}