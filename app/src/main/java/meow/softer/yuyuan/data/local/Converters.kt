package meow.softer.yuyuan.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun fromZonedDateTime(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toZonedDateTime(timestamp: Long?): ZonedDateTime? {
        return timestamp?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()) }
    }

    @TypeConverter
    fun fromBoolean(isActive: Boolean): Int {
        return if (isActive) 1 else 0
    }

    @TypeConverter
    fun toBoolean(value: Int): Boolean {
        return value != 0
    }
}