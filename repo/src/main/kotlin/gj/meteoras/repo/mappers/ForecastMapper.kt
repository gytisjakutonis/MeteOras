package gj.meteoras.repo.mappers

import gj.meteoras.data.Forecast
import gj.meteoras.net.data.ForecastNet
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun ForecastNet.Timestamp.toData(): Forecast.Timestamp? = try {
    Forecast.Timestamp(
        time = forecastTimeUtc?.toInstant()!!,
        airTemperature = airTemperature!!,
        feelsLikeTemperature = feelsLikeTemperature!!,
        windSpeed = windSpeed!!,
        windGust = windGust!!,
        windDirection = windDirection!!,
        cloudCover = cloudCover!!,
        seaLevelPressure = seaLevelPressure!!,
        relativeHumidity = relativeHumidity!!,
        totalPrecipitation = totalPrecipitation!!,
        condition = Forecast.Timestamp.Condition.values().firstOrNull { it.value == conditionCode }
            ?: Forecast.Timestamp.Condition.Null
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid timestamp: $this")
    null
}

fun ForecastNet.toData(): Forecast? = try {
    Forecast(
        place = place?.toData()!!,
        type = Forecast.Type.values().firstOrNull { it.value == forecastType }!!,
        creationTime = forecastCreationTimeUtc?.toInstant()!!,
        timestamps = forecastTimestamps?.mapNotNull { it.toData() }!!
    )
} catch (error: NullPointerException) {
    Timber.e("Invalid forecast: $this")
    null
}

private fun String.toInstant(): Instant? = try {
    timeFormatter.parse(this, LocalDateTime::from).toInstant(ZoneOffset.UTC)
} catch (error: DateTimeParseException) {
    null
}

private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
