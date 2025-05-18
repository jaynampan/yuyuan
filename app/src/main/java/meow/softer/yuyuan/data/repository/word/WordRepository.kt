package meow.softer.yuyuan.data.repository.word

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.WordDao
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.runBackgroundIO
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) : IWordRepository {
    override suspend fun getAllWords(): Result<List<Word>> {
        return runBackgroundIO { wordDao.getAll() }
    }

    override suspend fun getWordById(id: Int): Result<Word> {
        return runBackgroundIO { wordDao.getById(id) }
    }

    override suspend fun getWordsByIds(ids: List<Int>): Result<List<Word>> {
        return runBackgroundIO { wordDao.getByIds(ids) }
    }

    override suspend fun getWordsByBookId(bookId: Int): Result<List<Word>> {
        return runBackgroundIO { wordDao.getByBookId(bookId) }
    }

    override suspend fun getWordCountByBookId(bookId: Int): Result<Int> {
        return runBackgroundIO { wordDao.countByBookId(bookId) }
    }

    suspend fun getWordLearntCountByBook(bookId: Int): Result<Int> {
        return runBackgroundIO { wordDao.countWordLearntByBook(bookId) }
    }

    /**
     * Get the words whose id is larger than [startId]
     * @param startId will query id > [startId], it's not included
     */
    suspend fun getByBookWithLimit(startId: Int, bookId: Int, limit: Int): Result<List<Word>> {
        return runBackgroundIO {
            wordDao.getByBookWithLimit(startId, bookId, limit)
        }
    }

}