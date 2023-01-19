package gj.meteoras.db.type

import androidx.room.TypeConverter
import java.time.Instant

class TimeTypeConverters {

    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? = timestamp?.let {
        Instant.ofEpochMilli(it)
    }

    @TypeConverter
    fun toTimestamp(instant: Instant?): Long? = instant?.toEpochMilli()
}
