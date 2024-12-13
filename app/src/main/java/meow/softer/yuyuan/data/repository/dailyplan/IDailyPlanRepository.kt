package meow.softer.yuyuan.data.repository.dailyplan

import meow.softer.yuyuan.data.Result
import meow.softer.yuyuan.data.local.entiity.DailyPlan
import meow.softer.yuyuan.data.repository.setting.DefaultUserId
import java.time.ZonedDateTime

/**
 * Responsible for interacting with dailyPlan data
 */
interface IDailyPlanRepository {

    /**
     * Get all records from dailyPlan table
     */
    suspend fun getAllPlans(): Result<List<DailyPlan>>

    /**
     * Get a user's plans
     */
    suspend fun getPlansByUserId(userId: Int = DefaultUserId): Result<List<DailyPlan>>

    /**
     * Get a plan by dailyPlan id
     */
    suspend fun getPlanById(id: Int): Result<DailyPlan>

    /**
     * Create a day's daily plan
     * Default to now with default user
     */
    suspend fun create(
        date: ZonedDateTime = ZonedDateTime.now(),
        userId: Int = DefaultUserId
    ): Result<DailyPlan>

    /**
     * Clear all existing plans of all users
     */
    suspend fun clearAll(): Result<Unit>

    /**
     * Clear a user's plans
     */
    suspend fun clearByUserId(userId: Int = DefaultUserId): Result<Unit>
}