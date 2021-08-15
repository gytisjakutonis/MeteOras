package gj.meteoras.repo

import gj.meteoras.data.Forecast
import gj.meteoras.data.Place
import gj.meteoras.net.data.ForecastNet
import gj.meteoras.net.data.PlaceNet
import timber.log.Timber
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun List<PlaceNet>.toDao(): List<Place> = mapNotNull { it.toDao() }

fun PlaceNet.toDao(): Place? = try {
    Place(
        code = code!!,
        name = name!!,
        administrativeDivision = administrativeDivision,
        countryCode = countryCode!!,
        coordinates = coordinates?.let {
            Place.Coordinates(
                latitude = it.latitude!!,
                longitude = it.longitude!!
            )
        }!!,
        country = country
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid place: $this")
    null
}

fun ForecastNet.Timestamp.toDao(): Forecast.Timestamp? = try {
    Forecast.Timestamp(
        time = forecastTimeUtc?.toInstant()!!,
        airTemperature = airTemperature!!,
        windSpeed = windSpeed!!,
        windGust = windGust!!,
        windDirection = windDirection!!,
        cloudCover = cloudCover!!,
        seaLevelPressure = seaLevelPressure!!,
        relativeHumidity = relativeHumidity!!,
        totalPrecipitation = totalPrecipitation!!,
        condition = Forecast.Condition.values().firstOrNull { it.value == conditionCode }!!
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid timestamp: $this")
    null
}

fun ForecastNet.toDao(): Forecast? = try {
    Forecast(
        place = place?.toDao()!!,
        type = Forecast.Type.values().firstOrNull { it.value == forecastType }!!,
        creationTime = forecastCreationTimeUtc?.toInstant()!!,
        timestamps = forecastTimestamps?.mapNotNull { it.toDao() }!!
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid forecast: $this")
    null
}

private fun String.toInstant(): Instant? = try {
    timeFormatter.parse(this, Instant::from)
} catch (error: DateTimeParseException) {
    null
}

private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
