package meow.softer.yuyuan.data.repository.word

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.WordDao
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.runInBackground
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) : IWordRepository {
    override suspend fun getAllWords(): Result<List<Word>> {
        return runInBackground { wordDao.getAll() }
    }

    override suspend fun getWordById(id: Int): Result<Word> {
        return runInBackground { wordDao.getById(id) }
    }

    override suspend fun getWordsByIds(ids: List<Int>): Result<List<Word>> {
        return runInBackground { wordDao.getByIds(ids) }
    }

    override suspend fun getWordsByBookId(bookId: Int): Result<List<Word>> {
        return runInBackground { wordDao.getByBookId(bookId) }
    }

    override suspend fun getWordCountByBookId(bookId: Int): Result<Int> {
        return runInBackground { wordDao.countByBookId(bookId) }
    }
}