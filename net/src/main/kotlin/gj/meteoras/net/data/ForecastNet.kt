package gj.meteoras.net.data

import androidx.annotation.Keep

@Keep
data class ForecastNet(
    val place: PlaceNet?,
    val forecastType: String?,
    val forecastCreationTimeUtc: String?,
    val forecastTimestamps: List<Timestamp>?
) {
    @Keep
    data class Timestamp(
        val forecastTimeUtc: String?,
        val airTemperature: Double?,
        val windSpeed: Int?,
        val windGust: Int?,
        val windDirection: Int?,
        val cloudCover: Int?,
        val seaLevelPressure: Int?,
        val relativeHumidity: Int?,
        val totalPrecipitation: Double?,
        val conditionCode: String?
    )
}
