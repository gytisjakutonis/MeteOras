package gj.meteoras.data

data class Place(
    val code : String,
    val name : String,
    val normalisedName: String,
    val administrativeDivision : String? = null,
    val countryCode : String,
    val country: String? = null,
    val coordinates : Coordinates? = null
) {
    data class Coordinates(
        val latitude : Double,
        val longitude : Double
    )
}
