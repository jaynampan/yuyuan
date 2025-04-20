package meow.softer.yuyuan.data.repository.dailyplanword

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlanWord

/**
 * Responsible for interacting with dailyPlanWord data
 */
interface IDailyPlanWordRepository {
    suspend fun getAll(): Result<List<DailyPlanWord>>
    suspend fun getAllByPlanId(dailyPlanId: Int): Result<List<DailyPlanWord>>
    suspend fun clearAll(): Result<Unit>
    suspend fun clearByPlanId(dailyPlanId: Int): Result<Unit>
    suspend fun addToPlan(planId: Int, wordIds: List<Int>): Result<Unit>
    suspend fun addToPlan(planId: Int, wordId: Int): Result<Unit>
}