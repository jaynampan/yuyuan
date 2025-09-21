package meow.softer.yuyuan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import meow.softer.yuyuan.data.local.dao.BookDao
import meow.softer.yuyuan.data.local.dao.SentenceDao
import meow.softer.yuyuan.data.local.dao.WordDao
import meow.softer.yuyuan.data.local.dao.WordStatusDao
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word
import meow.softer.yuyuan.data.local.entiity.WordStatus

@Database(
    entities = [
        Word::class,
        Book::class,
        Sentence::class,
        WordStatus::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class YuyuanDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun sentenceDao(): SentenceDao
    abstract fun bookDao(): BookDao
    abstract fun wordStatusDao(): WordStatusDao

    companion object {
        @Volatile
        private var INSTANCE: YuyuanDatabase? = null
        private const val DB_NAME = "yuyuan.db"

        fun getDatabase(context: Context): YuyuanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YuyuanDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset(DB_NAME)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}