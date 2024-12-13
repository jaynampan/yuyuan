package meow.softer.yuyuan.data.repository.book

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.BookDao
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.data.repository.runInBackground
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) : IBookRepository {

    override suspend fun getAllBooks(): Result<List<Book>> {
        return runInBackground { bookDao.getAll() }

    }

    override suspend fun getBookById(id: Int): Result<Book> {
        return runInBackground { bookDao.getById(id) }
    }


}