package gj.meteoras.net.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gj.meteoras.net.NetConfig
import gj.meteoras.net.apiModule
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class MeteoApiTest : KoinTest {

    // https://medium.com/@eyalg/testing-androidx-room-kotlin-coroutines-2d1faa3e674f
    // Retrofit uses OkHttp threading, so does not work with runBlockingTest

    val server = MockWebServer()
    val meteo : MeteoApi by inject()

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { OkHttpClient.Builder().build() }
            },
            apiModule
        )
    }

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        server.start()
        mockkObject(NetConfig)
        every { NetConfig.meteoUrl } returns server.url("/").toString()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        server.shutdown()
        unmockkAll()
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun loadEmptyPlaces() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse().setBody(
                "[]"
            )
        }

        val places = runBlocking {
            meteo.places()
        }

        assertThat(places).isEmpty()
    }

    @Test
    fun loadSomePlaces() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse().setBody(
                """
                    [
                        {
                            "code":"abromiskes",
                            "name":"Abromi\u0161k\u0117s",
                            "administrativeDivision":"Elektr\u0117n\u0173 savivaldyb\u0117",
                            "countryCode":"LT"
                        },
                        {
                            "code":"acokavai",
                            "name":"Acokavai",
                            "countryCode":"LT"
                        }
                    ]
                """.trimIndent()
            )
        }

        val places = runBlocking {
            meteo.places()
        }

        assertThat(places.size).isEqualTo(2)
        assertThat(places[0].code).isEqualTo("abromiskes")
        assertThat(places[1].name).isEqualTo("Acokavai")
    }

    @Test
    fun loadPlace() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse().setBody(
                """
                    {
                        "code":"abromiskes",
                        "name":"Acokavai",
                        "administrativeDivision":"Elektr\u0117n\u0173 savivaldyb\u0117",
                        "countryCode":"LT",
                        "country":"Lietuva",
                        "coordinates":{
                            "latitude":1.1,
                            "longitude":2.2
                        }
                    }
                """.trimIndent()
            )
        }

        val place = runBlocking {
            meteo.place("code")
        }

        assertThat(place.code).isEqualTo("abromiskes")
        assertThat(place.name).isEqualTo("Acokavai")
        assertThat(place.country).isEqualTo("Lietuva")
        assertThat(place.coordinates?.latitude).isEqualTo(1.1)
    }

    @Test
    fun loadForecast() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) = MockResponse().setBody(
                """
                    {
                        "place":{
                            "code":"abromiskes",
                            "name":"Acokavai",
                            "administrativeDivision":"Elektr\u0117n\u0173 savivaldyb\u0117",
                            "countryCode":"LT",
                            "country":"Lietuva",
                            "coordinates":{
                                "latitude":1.1,
                                "longitude":2.2
                            }
                        },
                        "forecastType":"long-term",
                        "forecastCreationTimeUtc":"2021-08-24 07:52:26",
                        "forecastTimestamps":[
                            {
                                "forecastTimeUtc":"2021-08-24 08:00:00",
                                "airTemperature":16,
                                "windSpeed":4,
                                "windGust":8,
                                "windDirection":10,
                                "cloudCover":2,
                                "seaLevelPressure":1023,
                                "relativeHumidity":54,
                                "totalPrecipitation":0,
                                "conditionCode":"clear"
                            },
                            {
                                "forecastTimeUtc":"2021-08-24 09:00:00",
                                "airTemperature":16.9,
                                "windSpeed":4,
                                "windGust":9,
                                "windDirection":7,
                                "cloudCover":4,
                                "seaLevelPressure":1022,
                                "relativeHumidity":48,
                                "totalPrecipitation":0,
                                "conditionCode":"clear"
                            }
                        ]
                    }
                """.trimIndent()
            )
        }

        val forecast = runBlocking {
            meteo.forecast("code")
        }

        assertThat(forecast.place?.code).isEqualTo("abromiskes")
        assertThat(forecast.place?.name).isEqualTo("Acokavai")
        assertThat(forecast.forecastTimestamps?.get(0)?.windSpeed).isEqualTo(4)
        assertThat(forecast.forecastTimestamps?.get(0)?.conditionCode).isEqualTo("clear")
        assertThat(forecast.forecastTimestamps?.get(1)?.forecastTimeUtc).isEqualTo("2021-08-24 09:00:00")
    }
}
