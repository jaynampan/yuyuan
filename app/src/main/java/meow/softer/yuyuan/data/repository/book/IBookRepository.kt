package meow.softer.yuyuan.data.repository.book

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.Book

/**
 * Repo responsible for book data
 */
interface IBookRepository {
    suspend fun getAllBooks(): Result<List<Book>>
    suspend fun getBookById(id: Int): Result<Book>
}