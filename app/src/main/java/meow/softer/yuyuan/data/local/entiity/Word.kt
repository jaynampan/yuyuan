package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Word record data should not be modified
 *
 * CREATE TABLE [words](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [character] TEXT NOT NULL,
 *   [pinyin] TEXT NOT NULL,
 *   [char_audio_file] TEXT NOT NULL,
 *   [char_json_file] TEXT,
 *   [meaning] TEXT NOT NULL,
 *   [book_id] INTEGER NOT NULL REFERENCES [books]([id]));
 *
 */
@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,           // Reference to the `Book` entity
            parentColumns = ["id"],         // Column in `Book` table
            childColumns = ["book_id"],     // Column in `Words` table
            onDelete = ForeignKey.CASCADE   // Action when the referenced book is deleted
        )
    ],
    indices = [Index(value = arrayOf("book_id"))]
)
data class Word(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    val character: String,
    val pinyin: String,
    @ColumnInfo(name = "char_audio_file") val charAudioFile: String,
    @ColumnInfo(name = "char_json_file") val charJsonFile: String?,
    val meaning: String,
    @ColumnInfo(name = "book_id") val bookId: Int
)