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
    fun toDaoTimestamp() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNotNull
    }

    @Test
    fun toDaoTimestampNullIfNoWind() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            windSpeed = null,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfNoWindDirection() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = null,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfNoTemperature() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = null,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfNoTime() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = null,
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfBadTime() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "abc",
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "clear"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfNoCondition() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = null
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoTimestampNullIfBadCondition() {
        val net = ForecastNet.Timestamp(
            forecastTimeUtc = "2021-08-24 08:00:00",
            airTemperature = 1.1,
            windSpeed = 1,
            windGust = 2,
            windDirection = 100,
            cloudCover = 50,
            seaLevelPressure = 1000,
            relativeHumidity = 3,
            totalPrecipitation = 0.5,
            conditionCode = "something"
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecast() {
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

        val dao = net.toDao()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(1)
    }

    @Test
    fun toDaoForecastNollIfNoPlace() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastNullIfNoType() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastNullIfBadType() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastNullIfNoTime() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastNullIfBadTime() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastNullIfNoTimestamps() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoForecastEmptyTimestamps() {
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

        val dao = net.toDao()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(0)
    }

    @Test
    fun toDaoForecastSkipBadTimestamp() {
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

        val dao = net.toDao()

        assertThat(dao).isNotNull
        assertThat(dao?.timestamps?.size).isEqualTo(1)
    }
}
