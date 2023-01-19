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
        val feelsTemperature: Double,
        val windSpeed: Int,
        val windGust: Int,
        val windDirection: Int,
        val cloudCover: Int,
        val seaLevelPressure: Int,
        val relativeHumidity: Int,
        val totalPrecipitation: Double,
        val condition: Condition
    ) {
        enum class Condition(val value: String) {
            Clear("clear"),
            Cloudy("cloudy"),
            PartlyCloudy("partly-cloudy"),
            CloudyWithSunnyIntervals("cloudy-with-sunny-intervals"),
            Rain("rain"),
            LightRain("light-rain"),
            ModerateRain("moderate-rain"),
            HeavyRain("heavy-rain"),
            HeavyRainWithThunderstorms("heavy-rain-with-thunderstorms"),
            FreezingRain("freezing-rain"),
            Sleet("sleet"),
            LightSleet("light-sleet"),
            Hail("hail"),
            Thunder("thunder"),
            IsolatedThunderstorms("isolated-thunderstorms"),
            Thunderstorms("thunderstorms"),
            Snow("snow"),
            LightSnow("light-snow"),
            HeavySnow("heavy-snow"),
            Fog("fog"),
            Null("null")
        }
    }

    enum class Type(val value: String) {
        LongTerm("long-term")
    }
}
