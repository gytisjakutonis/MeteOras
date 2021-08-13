package gj.meteoras.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import gj.meteoras.data.Place

@Dao
interface PlacesDao {

    @Query("SELECT count(*) FROM place")
    suspend fun countAll(): Long

    @Query("SELECT * FROM place WHERE lower(name) LIKE lower(:name)")
    suspend fun findByName(name: String): List<Place>

    @Query("SELECT * FROM place WHERE code=:code")
    suspend fun findByCode(code: String): Place?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM place")
    suspend fun deleteAll()

    @Transaction
    suspend fun setAll(places: List<Place>) {
        deleteAll()
        insertAll(places)
    }

    @Update
    suspend fun update(place: Place)

    @Delete
    suspend fun delete(place: Place)

}
