package gj.meteoras.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gj.meteoras.db.converters.TimeTypeConverters
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.db.data.PlaceDb

@Database(
    version = 1,
    entities = [PlaceDb::class],
    exportSchema = false,
)
@TypeConverters(TimeTypeConverters::class)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}
