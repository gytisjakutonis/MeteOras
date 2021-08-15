package gj.meteoras.data

import java.time.Instant

data class Forecast(
    val place: Place,
    val type: Type,
    val creationTime: Instant,
    val timestamps: List<Timestamp>
) {
    data class Timestamp(
        val time: Instant,
        val airTemperature: Double,
        val windSpeed: Int,
        val windGust: Int,
        val windDirection: Int,
        val cloudCover: Int,
        val seaLevelPressure: Int,
        val relativeHumidity: Int,
        val totalPrecipitation: Double,
        val condition: Condition
    )

    enum class Type(val value: String) {
        LongTerm("long-term")
    }

    enum class Condition(val value: String) {
        Clear("clear"),
        IsolatedClound("isolated-clouds"),
        ScatteredClouds("scattered-clouds"),
        Overcast("overcast"),
        LightRain("light-rain"),
        ModerateRain("moderate-rain"),
        HeavyRain("heavy-rain"),
        Sleet("sleet"),
        LightSnow("light-snow"),
        ModerateSnow("moderate-snow"),
        HeavySnow("heavy-snow"),
        Fog("fog"),
        NA("na"),
    }
}
