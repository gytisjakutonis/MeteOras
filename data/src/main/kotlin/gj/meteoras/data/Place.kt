package gj.meteoras.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "place",
    indices = [Index("code", unique = true)]
)
data class Place(
    val code : String,
    val name : String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    @Embedded
    val coordinates : Coordinates? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    data class Coordinates(
        val latitude : Double,
        val longitude : Double
    )
}
