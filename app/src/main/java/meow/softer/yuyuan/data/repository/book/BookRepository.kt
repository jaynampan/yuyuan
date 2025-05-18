package meow.softer.yuyuan.data.repository.book

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.BookDao
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.data.repository.runBackgroundIO
import meow.softer.yuyuan.utils.debug
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) : IBookRepository {

    override suspend fun getAllBooks(): Result<List<Book>> {
        return runBackgroundIO { bookDao.getAll() }

    }

    override suspend fun getBookById(id: Int): Result<Book> {
        debug("get book by id invoked, id = $id")
        return runBackgroundIO { bookDao.getById(id) }
    }


}