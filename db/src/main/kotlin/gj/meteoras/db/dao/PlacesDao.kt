package gj.meteoras.db.dao

import androidx.room.*
import gj.meteoras.data.Place

@Dao
interface PlacesDao {

    @Query("SELECT count(*) FROM place")
    suspend fun countAll(): Long

    @Query("SELECT * FROM place where lower(name) like lower(:name)")
    suspend fun findByName(name: String): List<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM place")
    suspend fun deleteAll()

    @Transaction
    suspend fun setAll(places: List<Place>) {
        deleteAll()
        insertAll(places)
    }
}
