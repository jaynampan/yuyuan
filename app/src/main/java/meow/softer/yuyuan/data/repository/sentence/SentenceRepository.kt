package meow.softer.yuyuan.data.repository.sentence

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.SentenceDao
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.runBackgroundIO
import javax.inject.Inject

class SentenceRepository @Inject constructor(
    private val sentenceDao: SentenceDao
) : ISentenceRepository {
    override suspend fun getAllSentences(): Result<List<Sentence>> {
        return runBackgroundIO { sentenceDao.getAll() }
    }

    override suspend fun getSentenceById(id: Int): Result<Sentence> {
        return runBackgroundIO { sentenceDao.getById(id) }
    }

    override suspend fun getSentencesByIds(ids: List<Int>): Result<List<Sentence>> {
        return runBackgroundIO { sentenceDao.getByIds(ids) }
    }

    override suspend fun getSentenceByWord(word: Word): Result<Sentence> {
        return getSentenceByWordId(wordId = word.id)
    }

    override suspend fun getSentenceByWordId(wordId: Int): Result<Sentence> {
        return runBackgroundIO { sentenceDao.getByWordId(wordId) }
    }


    override suspend fun getSentenceByWordIds(wordIds: List<Int>): Result<List<Sentence>> {
        return runBackgroundIO { sentenceDao.getByWordIds(wordIds) }
    }
}