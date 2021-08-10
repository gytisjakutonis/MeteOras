package gj.meteoras.net.data

data class PlaceNet(
    val code: String?,
    val name: String?,
    val administrativeDivision: String? = null,
    val countryCode: String?,
    val country: String? = null,
    val coordinates: Coordinates? = null,
) {
    data class Coordinates(
        val latitude : Double?,
        val longitude : Double?
    )
}
