package gj.meteoras.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import gj.meteoras.db.data.PlaceDb

@Dao
interface PlacesDao {

    @Query("SELECT count(*) FROM place")
    suspend fun countAll(): Long

    @Query("SELECT * FROM place where name like :name")
    fun findAll(name: String): PagingSource<Int, PlaceDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<PlaceDb>)

    @Query("DELETE FROM place")
    suspend fun deleteAll()

    @Transaction
    suspend fun setAll(places: List<PlaceDb>) {
        deleteAll()
        insertAll(places)
    }
}
