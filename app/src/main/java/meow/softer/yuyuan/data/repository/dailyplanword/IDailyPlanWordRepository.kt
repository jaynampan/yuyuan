package meow.softer.yuyuan.data.repository.dailyplanword

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlanWord

/**
 * Responsible for interacting with dailyPlanWord data
 */
interface IDailyPlanWordRepository {
    suspend fun getAll(): Result<List<DailyPlanWord>>
    suspend fun getAllByPlanId(dailyPlanId: Int): Result<List<DailyPlanWord>>
    suspend fun addToPlan(wordId: Int): Result<Unit>
    suspend fun addToPlan(wordIds: List<Int>): Result<Unit>
    suspend fun clearAll(): Result<Unit>
    suspend fun clearByPlanId(dailyPlanId: Int): Result<Unit>
}