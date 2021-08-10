package gj.meteoras.repo.places

import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.time.Instant

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
    fun findPlacesLoadIfNotLoaded() {
        val timestamp = Instant.ofEpochSecond(0L)

        coEvery { meteoApi.places() } returns emptyList()
        coEvery { placesDao.findByName(any()) } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        runBlocking {
            repo.filterByName("name",)
        }

        coVerify { meteoApi.places() }
        coVerify { placesDao.findByName("name%") }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun findPlacesLoadIfExpired() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesTimeout.seconds).minusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        coEvery { placesDao.findByName(any()) } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        runBlocking {
            repo.filterByName("name",)
        }

        coVerify { meteoApi.places() }
        coVerify { placesDao.findByName("name%") }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun findPlacesNoLoadIfLoaded() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesTimeout.seconds).plusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        coEvery { placesDao.findByName(any()) } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        runBlocking {
            repo.filterByName("name",)
        }

        coVerify(exactly = 0) { meteoApi.places() }
        coVerify { placesDao.findByName("name%") }
        coVerify(exactly = 0) { placesDao.setAll(any()) }
        verify(exactly = 0) { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun findPlacesNoLoadIfLoading() {
        val timestamp = Instant.ofEpochSecond(0L)

        coEvery { meteoApi.places() } coAnswers {
            delay(1000 * 2L)
            emptyList()
        }
        coEvery { placesDao.findByName(any()) } returns emptyList()
        every { repoPreferences.placesTimestamp } returns timestamp

        runBlocking {
            val one = launch { repo.filterByName("name") }
            val two = launch { repo.filterByName("eman") }

            joinAll(one, two)
        }

        coVerify(exactly = 1) { meteoApi.places() }
        coVerify { placesDao.findByName("name%") }
        coVerify { placesDao.findByName("eman%") }
    }
}
