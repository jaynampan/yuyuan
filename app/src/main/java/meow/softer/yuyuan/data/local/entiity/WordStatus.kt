package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

/**
 * CREATE TABLE [word_status](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [word_id] INTEGER NOT NULL REFERENCES [words][id],
 *   [user_id] INTEGER NOT NULL REFERENCES [users][id],
 *   [is_learnt] BOOLEAN DEFAULT False,
 *   [isStarred] BOOLEAN DEFAULT False,
 *   [timeLearned] TIMESTAMP);
 *
 */
@Entity(
    tableName = "word_status",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(
        value = arrayOf("word_id", "user_id"),
        unique = true
    )]  // Enforcing uniqueness of (word_id, user_id)
)
data class WordStatus(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "word_id") val wordId: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "is_learnt") val isLearnt: Boolean = false,
    @ColumnInfo(name = "is_starred") val isStarred: Boolean = false,
    @ColumnInfo(name = "time_learned") val timeLearned: ZonedDateTime? = null
)