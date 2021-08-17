package gj.meteoras.data

import android.icu.text.Normalizer2
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.regex.Pattern

@Entity(
    tableName = "place",
    indices = [Index("code", unique = true)]
)
data class Place(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val code : String,
    val name : String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    val country: String? = null,
    @Embedded
    val coordinates : Coordinates? = null
) {

    val complete: Boolean get() = country != null && coordinates != null
    var normalisedName: String? = Normalizer2.getNFKDInstance().normalize(name)
        .replace(InCombiningDiacriticalMarks, "")

    data class Coordinates(
        val latitude : Double,
        val longitude : Double
    )
}

private val InCombiningDiacriticalMarks = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").toRegex()
