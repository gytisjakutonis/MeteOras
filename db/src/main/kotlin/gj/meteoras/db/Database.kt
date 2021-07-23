package gj.meteoras.db

import androidx.room.Database
import androidx.room.RoomDatabase
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}
