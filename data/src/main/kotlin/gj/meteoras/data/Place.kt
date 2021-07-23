package gj.meteoras.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("code", unique = true)])
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val code : String,
    val name : String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    @Embedded
    val coordinates : Coordinates? = null
) {
    data class Coordinates(
        val latitude : Double,
        val longitude : Double
    )
}
