package gj.meteoras.repo.mappers

import gj.meteoras.ext.lang.normalise
import gj.meteoras.net.data.ForecastNet
import gj.meteoras.net.data.PlaceNet
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ForecastMapperTest {

    @Before
    fun before() {
        mockkStatic(String::normalise)
        every { any<String>().normalise() } returns "normalised"
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun toDataTimestamp() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNotNull
    }

    @Test
    fun toDataTimestampNullIfNoWind() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = null,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfNoWindDirection() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = null,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfNoTemperature() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = null,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfNoFeelsTemperature() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = null,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfNoTime() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = null,
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfBadTime() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "abc",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfNoCondition() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = null
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataTimestampNullIfBadCondition() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            feelsLikeTemperature = 2.2,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "something"
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecast() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(1)
    }

    @Test
    fun toDataForecastNollIfNoPlace() {
        val net = ForecastNet(
            place = PlaceNet(
                code = null,
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastNullIfNoType() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = null,
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastNullIfBadType() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "short-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastNullIfNoTime() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = null,
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastNullIfBadTime() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "abc",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastNullIfNoTimestamps() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = null
        )

        val dao = net.toData()

        assertThat(dao).isNull()
    }

    @Test
    fun toDataForecastEmptyTimestamps() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = emptyList()
        )

        val dao = net.toData()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(0)
    }

    @Test
    fun toDataForecastSkipBadTimestamp() {
        val net = ForecastNet(
            place = PlaceNet(
                code = "code",
                name = "name",
                countryCode = "cc",
            ),
            forecastType = "long-term",
            forecastCreationTimeUtc = "2021-08-24 08:00:00",
            forecastTimestamps = listOf(
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "abc"
                ),
                ForecastNet.Timestamp(
                    forecastTimeUtc = "2021-08-24 08:00:00",
                    airTemperature = 1.1,
                    feelsLikeTemperature = 2.2,
                    windSpeed = 1,
                    windGust = 2,
                    windDirection = 100,
                    cloudCover = 50,
                    seaLevelPressure = 1000,
                    relativeHumidity = 3,
                    totalPrecipitation = 0.5,
                    conditionCode = "clear"
                )
            )
        )

        val dao = net.toData()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(1)
    }
}
