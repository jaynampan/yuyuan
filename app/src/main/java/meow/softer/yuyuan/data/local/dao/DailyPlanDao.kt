package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import meow.softer.yuyuan.data.local.entiity.DailyPlan

@Dao
interface DailyPlanDao {
    @Query("SELECT * FROM daily_plan")
    fun getAll(): List<DailyPlan>

    @Query("SELECT * FROM daily_plan WHERE user_id = :userId")
    fun getByUserId(userId: Int): List<DailyPlan>

    @Query("SELECT * FROM daily_plan WHERE id = :id")
    fun getById(id: Int): DailyPlan

    @Query("DELETE FROM daily_plan")
    fun deleteAll()

    @Query("DELETE FROM daily_plan WHERE id IN (:userId)")
    fun deleteByUserId(userId: Int)

    @Insert
    fun insert(dailyPlan: DailyPlan):Long
    @Delete
    fun delete(dailyPlan: DailyPlan)
}