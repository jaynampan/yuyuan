package meow.softer.yuyuan.data.repository.word

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.Word

/**
 * Repo responsible for word data
 *
 * Word data should not be modified during learning
 */
interface IWordRepository {
    suspend fun getAllWords(): Result<List<Word>>
    suspend fun getWordById(id: Int): Result<Word>
    suspend fun getWordsByIds(ids: List<Int>): Result<List<Word>>
    suspend fun getWordsByBookId(bookId: Int): Result<List<Word>>
    // count a book's words
    suspend fun getWordCountByBookId(bookId: Int): Result<Int>

}