package gj.meteoras.db

import androidx.room.Database
import androidx.room.RoomDatabase
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.db.data.PlaceDb

@Database(entities = [PlaceDb::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun places(): PlacesDao
}
