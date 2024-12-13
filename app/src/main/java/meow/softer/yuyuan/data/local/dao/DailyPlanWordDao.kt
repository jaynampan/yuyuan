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

    @Insert
    fun insertAll(vararg dailyPlanWords: DailyPlanWord)

    @Delete
    fun delete(dailyPlanWord: DailyPlanWord)
}