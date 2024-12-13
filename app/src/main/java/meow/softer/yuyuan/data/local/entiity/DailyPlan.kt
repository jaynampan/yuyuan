package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

/**
 * CREATE TABLE [daily_plan](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [user_id] INTEGER NOT NULL REFERENCES [users]([id]),
 *   [date] DATE NOT NULL);
 *
 */
@Entity(
    tableName = "daily_plan",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    val date: ZonedDateTime  
)