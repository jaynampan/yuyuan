package meow.softer.yuyuan.data.repository.sentence

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word


/**
 * Responsible for interacting with sentence data
 */
interface ISentenceRepository {
    suspend fun getAllSentences(): Result<List<Sentence>>
    suspend fun getSentenceById(id: Int): Result<Sentence>
    suspend fun getSentenceByWord(word: Word): Result<Sentence>
    suspend fun getSentenceByWordId(wordId: Int): Result<Sentence>
    suspend fun getSentenceByWordIds(wordIds: List<Int>): Result<List<Sentence>>
    suspend fun getSentencesByIds(ids: List<Int>): Result<List<Sentence>>
}