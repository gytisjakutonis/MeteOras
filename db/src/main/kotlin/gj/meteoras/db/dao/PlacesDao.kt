package gj.meteoras.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import gj.meteoras.db.data.PlaceDb

@Dao
interface PlacesDao {

    @Query("SELECT count(*) FROM place")
    suspend fun countAll(): Long

    @Query("SELECT * FROM place WHERE lower(normalisedName) LIKE lower(:name)")
    suspend fun findByName(name: String): List<PlaceDb>

    @Query("SELECT * FROM place WHERE code=:code")
    suspend fun findByCode(code: String): PlaceDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<PlaceDb>)

    @Query("DELETE FROM place")
    suspend fun deleteAll()

    @Transaction
    suspend fun setAll(places: List<PlaceDb>) {
        deleteAll()
        insertAll(places)
    }

    @Insert
    suspend fun insert(place: PlaceDb): Long

    @Update
    suspend fun update(place: PlaceDb)

    @Delete
    suspend fun delete(place: PlaceDb)

}
