package meow.softer.yuyuan.data.local.entiity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
)