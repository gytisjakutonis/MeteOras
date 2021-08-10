package gj.meteoras.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.room.TimeTypeConverters

@Database(entities = [Place::class], version = 1, exportSchema = false)
@TypeConverters(TimeTypeConverters::class)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}
