package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * CREATE TABLE [users](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [current_book_id] INTEGER NOT NULL REFERENCES [books]([id]));
 *
 */
@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["current_book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "current_book_id") val currentBookId: Int
)
