package gj.meteoras.db

import androidx.room.Database
import androidx.room.RoomDatabase
import gj.meteoras.data.Place

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}