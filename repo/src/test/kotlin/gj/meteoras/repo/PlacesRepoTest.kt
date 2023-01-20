package gj.meteoras.repo

import gj.meteoras.data.Forecast
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.ext.lang.normalise
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.net.data.ForecastNet
import gj.meteoras.net.data.PlaceNet
import gj.meteoras.test.TimberRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.time.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class PlacesRepoTest : KoinTest {

    @MockK
    private lateinit var meteoApi: MeteoApi

    @MockK
    private lateinit var placesDao: PlacesDao

    @MockK
    private lateinit var repoPreferences: RepoPreferences

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { meteoApi }
                single { placesDao }
                single { repoPreferences }
                single { PlacesRepo(get(), get(), get()) }
            }
        )
    }

    @get:Rule
    val timberRule = TimberRule()

    val repo : PlacesRepo by inject()

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        mockkObject(RepoConfig)
        Dispatchers.setMain(dispatcher)
        mockkStatic(String::normalise)
        every { any<String>().normalise() } returns "normalised"
    }

    @After
    fun after() {
        unmockkAll()
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun findPlaces() {
        coEvery { placesDao.findByName(any()) } returns emptyList()

        val result = runBlocking {
            repo.filterByName("name",)
        }

        coVerify(exactly = 0) { meteoApi.places() }
        coVerify { placesDao.findByName("name%") }
    }

    @Test
    fun findPlacesError() {
        coEvery { placesDao.findByName(any()) } throws RuntimeException()

        val result = runBlocking {
            repo.filterByName("name",)
        }

        assertThat(result.isFailure).isTrue
    }

    @Test
    fun syncPlacesInitial() {
        val timestamp = Instant.ofEpochSecond(0L)

        coEvery { meteoApi.places() } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isSuccess).isTrue
        coVerify { meteoApi.places() }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun syncPlacesExpired() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesTimeout.seconds).minusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isSuccess).isTrue
        coVerify { meteoApi.places() }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun syncPlacesValid() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesTimeout.seconds).plusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        coEvery { placesDao.countAll() } returns 10
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isSuccess).isTrue
        coVerify(exactly = 0) { meteoApi.places() }
        coVerify(exactly = 0) { placesDao.setAll(any()) }
        verify(exactly = 0) { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun syncPlacesError() {
        val timestamp = Instant.ofEpochSecond(0L)

        coEvery { meteoApi.places() } throws RuntimeException()
        coEvery { placesDao.findByName(any()) } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isFailure).isTrue
        coVerify(exactly = 1) { meteoApi.places() }
    }

    @Test
    fun getForecastSuccess() {
        coEvery { meteoApi.forecast(any()) } returns ForecastNet(
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

        val result = runBlocking {
            repo.getForecast("code")
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()?.type).isEqualTo(Forecast.Type.LongTerm)
        assertThat(result.getOrNull()?.timestamps?.size).isEqualTo(1)
    }

    @Test
    fun getForecastInvalid() {
        coEvery { meteoApi.forecast(any()) } returns ForecastNet(
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

        val result = runBlocking {
            repo.getForecast("code")
        }

        assertThat(result.isSuccess).isFalse
    }

    @Test
    fun getForecastFailure() {
        coEvery { meteoApi.forecast(any()) } throws RuntimeException()

        val result = runBlocking {
            repo.getForecast("code")
        }

        assertThat(result.isSuccess).isFalse
    }
}
