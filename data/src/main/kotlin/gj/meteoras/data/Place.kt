package gj.meteoras.data

data class Place(
    val code: String,
    val name: String,
    val administrativeDivision: String? = null,
    val countryCode: String,
)
