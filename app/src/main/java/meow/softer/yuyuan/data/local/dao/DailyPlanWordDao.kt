package meow.softer.yuyuan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import meow.softer.yuyuan.data.local.entiity.DailyPlanWord

@Dao
interface DailyPlanWordDao {
    @Query("SELECT * FROM daily_plan_words")
    fun getAll(): List<DailyPlanWord>

    @Query("SELECT * FROM daily_plan_words WHERE daily_plan_id = :dailyPlanId")
    fun getAllByPlanId(dailyPlanId: Int): List<DailyPlanWord>

    @Query("DELETE FROM daily_plan_words")
    fun deleteAll()

    @Query("DELETE FROM daily_plan_words WHERE daily_plan_id = :dailyPlanId")
    fun deleteByPlanId(dailyPlanId: Int)

    @Insert
    fun insertAll(dailyPlanWords: List<DailyPlanWord>)

    @Insert
    fun insert(dailyPlanWord: DailyPlanWord)
    @Query("INSERT INTO daily_plan_words VALUES(:planId, :wordId)")
    fun insert(planId:Int, wordId:Int)

    @Delete
    fun delete(dailyPlanWord: DailyPlanWord)
}