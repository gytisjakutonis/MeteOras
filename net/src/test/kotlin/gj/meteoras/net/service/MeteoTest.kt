package gj.meteoras.net.service

import gj.meteoras.net.NetConfig
import gj.meteoras.net.restModule
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
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

class MeteoTest : KoinTest {

    val server = MockWebServer()
    val meteo : Meteo by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { OkHttpClient.Builder().build() }
            },
            restModule
        )
    }

    @Before
    fun before() {
        server.start()
        mockkObject(NetConfig)
        every { NetConfig.meteoUrl } returns server.url("/").toString()
    }

    @After
    fun after() {
        unmockkAll()
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
}
