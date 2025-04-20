package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * CREATE TABLE [daily_plan_words](
 *   [daily_plan_id] INTEGER NOT NULL REFERENCES [daily_plan]([id]),
 *   word_id INTEGER NOT NULL REFERENCES [words]([word_id]),
 *   PRIMARY KEY(daily_plan_id, word_id));
 *
 */
@Entity(
    tableName = "daily_plan_words",
    foreignKeys = [
        ForeignKey(
            entity = DailyPlan::class,
            parentColumns = ["id"],
            childColumns = ["daily_plan_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["daily_plan_id", "word_id"]
)
data class DailyPlanWord(
    @ColumnInfo(name = "daily_plan_id") val dailyPlanId: Int,   // Foreign key referencing daily_plan.id
    @ColumnInfo(name = "word_id") val wordId: Int          // Foreign key referencing words.id
)