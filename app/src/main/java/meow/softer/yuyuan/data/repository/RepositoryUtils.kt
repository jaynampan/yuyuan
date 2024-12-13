package meow.softer.yuyuan.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import meow.softer.yuyuan.data.Result

/**
 * Specifically for repository's IO operations
 * Handles YuResult automatically with coroutine
 */
suspend fun <T> runInBackground(block: suspend () -> T): Result<T> {
    return try {
        val result = withContext(Dispatchers.IO) { block() }
        Result.Success(result)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
