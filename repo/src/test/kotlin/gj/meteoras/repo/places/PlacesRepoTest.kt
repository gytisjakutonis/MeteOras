package gj.meteoras.repo.places

import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.net.data.PlaceNet
import gj.meteoras.repo.PlacesRepo
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
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

    val repo : PlacesRepo by inject()

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        mockkObject(RepoConfig)
        Dispatchers.setMain(dispatcher)
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

        assertThat(result.isSuccess).isTrue
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
        assertThat(result.getOrNull()).isTrue
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
        assertThat(result.getOrNull()).isTrue
        coVerify { meteoApi.places() }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun syncPlacesValid() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesTimeout.seconds).plusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isFalse
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
    fun getPlaceComplete() {
        coEvery { meteoApi.place(any()) } returns PlaceNet(
            code = "code",
            name = "name",
            countryCode = "cc",
            country = "other",
            coordinates = PlaceNet.Coordinates(
                latitude = 3.3,
                longitude = 4.4
            )
        )

        coEvery { placesDao.findByCode(any()) } returns Place(
            id = 123,
            code = "code",
            name = "name",
            countryCode = "cc",
            country = "country",
            coordinates = Place.Coordinates(
                latitude = 1.1,
                longitude = 2.2
            )
        )

        val result = runBlocking {
            repo.getPlace("code")
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()?.country).isEqualTo("country")
        coVerify(exactly = 0) { meteoApi.place(any()) }
        coVerify(exactly = 0) { placesDao.update(any()) }
        coVerify(exactly = 0) { placesDao.insert(any()) }
    }

    @Test
    fun getPlaceIncomplete() {
        coEvery { meteoApi.place(any()) } returns PlaceNet(
            code = "code",
            name = "name",
            countryCode = "cc",
            country = "other",
            coordinates = PlaceNet.Coordinates(
                latitude = 3.3,
                longitude = 4.4
            )
        )

        coEvery { placesDao.findByCode(any()) } returns Place(
            id = 123,
            code = "code",
            name = "name",
            countryCode = "cc",
            country = "country",
        )

        val result = runBlocking {
            repo.getPlace("code")
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()?.country).isEqualTo("other")
        coVerify { meteoApi.place("code") }
        coVerify { placesDao.update(withArg { assertThat(it.id).isEqualTo(123) }) }
        coVerify(exactly = 0) { placesDao.insert(any()) }
    }

    @Test
    fun getPlaceMissing() {
        coEvery { meteoApi.place(any()) } returns PlaceNet(
            code = "code",
            name = "name",
            countryCode = "cc",
            country = "other",
            coordinates = PlaceNet.Coordinates(
                latitude = 3.3,
                longitude = 4.4
            )
        )

        coEvery { placesDao.findByCode(any()) } returns null
        coEvery { placesDao.insert(any()) } returns 123

        val result = runBlocking {
            repo.getPlace("code")
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()?.country).isEqualTo("other")
        coVerify { meteoApi.place("code") }
        coVerify(exactly = 0) { placesDao.update(any()) }
        coVerify { placesDao.insert(withArg { assertThat(it.code).isEqualTo("code") }) }
    }

    @Test
    fun getPlaceInvalid() {
        coEvery { meteoApi.place(any()) } returns PlaceNet(
            code = null,
            name = "name",
            countryCode = "cc",
            country = "other",
            coordinates = PlaceNet.Coordinates(
                latitude = 3.3,
                longitude = 4.4
            )
        )

        coEvery { placesDao.findByCode(any()) } returns null

        val result = runBlocking {
            repo.getPlace("code")
        }

        assertThat(result.isSuccess).isTrue
        assertThat(result.getOrNull()).isNull()
        coVerify(exactly = 0) { placesDao.update(any()) }
        coVerify(exactly = 0) { placesDao.insert(any()) }
    }

    @Test
    fun getPlaceError() {
        coEvery { meteoApi.place(any()) } throws RuntimeException()

        coEvery { placesDao.findByCode(any()) } returns null

        val result = runBlocking {
            repo.getPlace("code")
        }

        assertThat(result.isSuccess).isFalse
        coVerify(exactly = 0) { placesDao.update(any()) }
        coVerify(exactly = 0) { placesDao.insert(any()) }
    }
}
