package gj.meteoras.repo.mappers

import gj.meteoras.data.Place
import gj.meteoras.ext.lang.normalise
import gj.meteoras.net.data.PlaceNet
import timber.log.Timber

fun List<PlaceNet>.toDao(): List<Place> = mapNotNull { it.toDao() }

fun PlaceNet.toDao(): Place? = try {
    Place(
        code = code!!,
        name = name!!,
        normalisedName = name!!.normalise(),
        administrativeDivision = administrativeDivision,
        countryCode = countryCode!!,
        coordinates = coordinates?.let {
            Place.Coordinates(
                latitude = it.latitude!!,
                longitude = it.longitude!!
            )
        },
        country = country
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid place: $this")
    null
}
