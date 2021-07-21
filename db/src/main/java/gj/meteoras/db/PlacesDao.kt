package gj.meteoras.db

import androidx.paging.PagingSource
import androidx.room.*
import gj.meteoras.data.Place

@Dao
interface PlacesDao {

    @Query("SELECT count(*) FROM Place")
    suspend fun countAll(): Long

    @Query("SELECT * FROM Place where name like :name")
    fun findAll(name: String): PagingSource<Int, Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)

    @Query("DELETE FROM Place")
    suspend fun deleteAll()

    @Transaction
    suspend fun setAll(places: List<Place>) {
        deleteAll()
        insertAll(places)
    }
}
