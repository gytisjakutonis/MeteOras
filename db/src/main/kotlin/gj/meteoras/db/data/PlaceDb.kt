package gj.meteoras.db.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "place",
    indices = [Index("code", unique = true)]
)
data class PlaceDb(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val code : String,
    val name : String,
    val normalisedName: String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    val country: String? = null,
    @Embedded
    val coordinates : Coordinates? = null
) {
    val complete: Boolean get() = country != null && coordinates != null

    data class Coordinates(
        val latitude : Double,
        val longitude : Double
    )
}
