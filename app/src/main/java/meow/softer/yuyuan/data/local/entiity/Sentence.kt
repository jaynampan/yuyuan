package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

/**
 * CREATE TABLE [sentences](
 *   [id] INTEGER PRIMARY KEY AUTOINCREMENT,
 *   [word_id] INTEGER NOT NULL REFERENCES [words][id],
 *   [sentence] TEXT NOT NULL,
 *   [translation] TEXT NOT NULL,
 *   [sentence_pinyin] TEXT NOT NULL,
 *   [sentence_audio_file] TEXT NOT NULL,
 *   [created_at] TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
 *
 */
@Entity(
    tableName = "sentences",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = arrayOf("word_id"), unique = true)]
)
data class Sentence(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "word_id") val wordId: Int,
    @ColumnInfo(name = "sentence")val content: String,
    val translation: String,
    @ColumnInfo(name = "sentence_pinyin") val pinyin: String,
    @ColumnInfo(name = "sentence_audio_file") val audioFile: String,
    @ColumnInfo(name = "created_at") val createdAt: ZonedDateTime
)