package meow.softer.yuyuan.data.repository.sentence

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.dao.SentenceDao
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.repository.runInBackground
import javax.inject.Inject

class SentenceRepository @Inject constructor(
    private val sentenceDao: SentenceDao
) : ISentenceRepository {
    override suspend fun getAllSentences(): Result<List<Sentence>> {
        return runInBackground { sentenceDao.getAll() }
    }

    override suspend fun getSentenceById(id: Int): Result<Sentence> {
        return runInBackground { sentenceDao.getById(id) }
    }

    override suspend fun getSentencesByIds(ids: List<Int>): Result<List<Sentence>> {
        return runInBackground { sentenceDao.getByIds(ids) }
    }

    override suspend fun getSentenceByWord(word: Word): Result<Sentence> {
        return getSentenceByWordId(wordId = word.id)
    }

    override suspend fun getSentenceByWordId(wordId: Int): Result<Sentence> {
        return runInBackground { sentenceDao.getByWordId(wordId) }
    }


    override suspend fun getSentenceByWordIds(wordIds: List<Int>): Result<List<Sentence>> {
        return runInBackground { sentenceDao.getByWordIds(wordIds) }
    }
}