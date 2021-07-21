package gj.meteoras.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(indices = [Index("code", unique = true)])
data class Place(
    @PrimaryKey(autoGenerate = true)
    @Expose(deserialize = false, serialize = false)
    val id : Int = 0,
    val code : String,
    val name : String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    @Embedded
    val coordinates : Coordinates? = null
)
