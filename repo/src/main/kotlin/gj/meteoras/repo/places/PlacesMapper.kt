package gj.meteoras.repo.places

import gj.meteoras.data.Place
import gj.meteoras.net.data.PlaceNet
import timber.log.Timber

fun List<PlaceNet>.toDao(): List<Place> = mapNotNull { it.toDao() }

fun PlaceNet.toDao() : Place? = toDaoOrNull()
    ?: run {
        Timber.e("Invalid place: $this")
        null
    }

private fun PlaceNet.toDaoOrNull() : Place? {
    return Place(
        code = code ?: return null,
        name = name ?: return null,
        administrativeDivision = administrativeDivision,
        countryCode = countryCode ?: return null,
        coordinates = coordinates?.let {
            Place.Coordinates(
                latitude = it.latitude ?: return null,
                longitude = it.longitude ?: return null
            )
        },
        country = country
    )
}
