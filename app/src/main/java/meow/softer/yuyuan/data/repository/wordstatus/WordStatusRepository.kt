package meow.softer.yuyuan.data.repository.wordstatus

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.WordStatusDao
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.local.entiity.WordStatus
import meow.softer.yuyuan.data.repository.runBackgroundIO
import java.time.ZonedDateTime
import javax.inject.Inject


class WordStatusRepository @Inject constructor(
    private val wordStatusDao: WordStatusDao
) : IWordStatusRepository {

    override suspend fun getAllWordStatus(): Result<List<WordStatus>> {
        return runBackgroundIO { wordStatusDao.getAll() }
    }



    override suspend fun getWordStatusByWordId(wordId: Int): Result<WordStatus> {
        return runBackgroundIO { wordStatusDao.getByWordId(wordId) }
    }

    override suspend fun getWordStatusById(id: Int): Result<WordStatus> {
        return runBackgroundIO { wordStatusDao.getById(id) }
    }

    override suspend fun getLearntList(): Result<List<WordStatus>> {
        return runBackgroundIO { wordStatusDao.getLearnt() }
    }

    override suspend fun getStarredList(): Result<List<WordStatus>> {
        return runBackgroundIO { wordStatusDao.getStarred() }
    }

    override suspend fun getLearntListByDate(
        date: ZonedDateTime,
    ): Result<List<WordStatus>> {
        return runBackgroundIO { wordStatusDao.getLearntByDate(date) }
    }

    override suspend fun getLearntListByInterval(
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Result<List<WordStatus>> {
        return runBackgroundIO { wordStatusDao.getLearntByInterval(start, end) }
    }

    override suspend fun toggleStarred(word: Word): Result<Unit> {
        return toggleStarredByWordId(word.id)
    }


    override suspend fun toggleStarred(wordStatus: WordStatus): Result<Unit> {
        return toggleStarredByStatusId(wordStatus.id)
    }


    override suspend fun toggleStarredByStatusId(wordStatusId: Int): Result<Unit> {
        return runBackgroundIO { wordStatusDao.toggleStarredByStatusId(wordStatusId) }
    }

    override suspend fun toggleStarredByWordId(wordId: Int): Result<Unit> {
        return runBackgroundIO { wordStatusDao.toggleStarred(wordId) }
    }

    override suspend fun setLearntByWordId(wordId: Int): Result<Unit> {
        return runBackgroundIO {
            wordStatusDao.setLearntByWordId(
                wordId, dateTime = ZonedDateTime.now()
            )
        }
    }

    override suspend fun setLearnt(word: Word): Result<Unit> {
        return setLearntByWordId(word.id)
    }

    override suspend fun setLearnt(wordStatus: WordStatus): Result<Unit> {
        return setLearntByStatusId(wordStatus.id)
    }


    override suspend fun setLearntByStatusId(statusId: Int): Result<Unit> {
        return runBackgroundIO {
            wordStatusDao.setLearntByStatusId(
                statusId, dateTime = ZonedDateTime.now()
            )
        }
    }



    override suspend fun getNewWordList(limit: Int?): Result<List<WordStatus>> {
        return runBackgroundIO {
            if (limit == null) {
                wordStatusDao.getAllNew()
            } else {
                wordStatusDao.getNewWithLimit(limit = limit)
            }

        }
    }

    override suspend fun getRandomNewWordIds(limit: Int): Result<List<Int>> {
        return runBackgroundIO {
            if(limit <= 0){
               throw IllegalArgumentException("limit must be positive Int.")
            }
            wordStatusDao.getRandomNewWordIds(limit = limit)
        }
    }

    override suspend fun getRandomNewWordIds(
        bookId: Int,
        limit: Int,

    ): Result<List<Int>> {
        return runBackgroundIO {
            if(bookId <= 0 || limit <= 0){
                throw IllegalArgumentException("bookId and limit must be positive Int.")
            }
            wordStatusDao.getRandomNewWordIdsByBook(bookId, limit)
        }
    }

    suspend fun getNewestLearntWordIdByBook(bookId: Int):Result<Int>{
        return  runBackgroundIO {
            wordStatusDao.getNewestLearntWordIdByBook(bookId)
        }
    }
}