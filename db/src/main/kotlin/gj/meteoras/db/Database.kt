package gj.meteoras.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.db.data.PlaceDb
import gj.meteoras.db.type.TimeTypeConverters

@Database(
    version = 2,
    entities = [PlaceDb::class],
    exportSchema = false,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(TimeTypeConverters::class)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}
