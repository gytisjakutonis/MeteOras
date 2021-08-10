package gj.meteoras.repo.places

import gj.meteoras.data.Place
import gj.meteoras.net.data.PlaceNet
import timber.log.Timber

fun List<PlaceNet>.toDao(): List<Place> = mapNotNull { placeNet ->
    placeNet.toDao() ?: run {
        Timber.e("Invalid place: $placeNet")
        null
    }
}

fun PlaceNet.toDao() : Place? {
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
