package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime


/**
 *
 * CREATE TABLE [books](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [book_title] TEXT NOT NULL,
 *   [book_description] TEXT,
 *   [book_icon_file] TEXT NOT NULL,
 *   [created_at] TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "book_title") val bookTitle: String,
    @ColumnInfo(name = "book_description") val bookDescription: String?,
    @ColumnInfo(name = "book_icon_file") val bookIconFile: String,
    @ColumnInfo(name = "created_at") val createdAt: ZonedDateTime
)
