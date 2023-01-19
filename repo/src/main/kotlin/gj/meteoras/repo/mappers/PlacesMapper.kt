package gj.meteoras.repo.mappers

import gj.meteoras.data.Place
import gj.meteoras.db.data.PlaceDb
import gj.meteoras.ext.lang.normalise
import gj.meteoras.net.data.PlaceNet
import timber.log.Timber

fun PlaceNet.toData(): Place? = try {
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

fun PlaceNet.toDb(): PlaceDb? = try {
    PlaceDb(
        code = code!!,
        name = name!!,
        normalisedName = name!!.normalise(),
        administrativeDivision = administrativeDivision,
        countryCode = countryCode!!,
        coordinates = coordinates?.let {
            PlaceDb.Coordinates(
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

fun PlaceDb.toData() = Place(
    code = code,
    name = name,
    normalisedName = normalisedName,
    administrativeDivision = administrativeDivision,
    countryCode = countryCode,
    coordinates = coordinates?.let {
        Place.Coordinates(
            latitude = it.latitude,
            longitude = it.longitude
        )
    },
    country = country
)
