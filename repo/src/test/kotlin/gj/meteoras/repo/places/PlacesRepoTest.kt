package gj.meteoras.repo.places

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import gj.meteoras.data.Place
import gj.meteoras.db.dao.PlacesDao
import gj.meteoras.net.api.MeteoApi
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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

    @MockK
    private lateinit var pagingSource: PagingSource<Int, Place>

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
        every { placesDao.findAll(any()) } returns pagingSource
        every { repoPreferences.placesTimestamp } returns timestamp

        val pager = runBlocking {
            with(repo) {
                findPlaces(
                    "name",
                    PagingConfig(
                        pageSize = 2,
                        prefetchDistance = 2,
                        maxSize = 10,
                        jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
                    )
                )
            }
        }

        runBlocking {
            pager.flow.take(1).toList()
        }

        coVerify { meteoApi.places() }
        verify { placesDao.findAll("name%") }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun findPlacesLoadIfExpired() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesExpirySeconds).minusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        every { placesDao.findAll(any()) } returns pagingSource
        every { repoPreferences.placesTimestamp } returns timestamp

        val pager = runBlocking {
            with(repo) {
                findPlaces(
                    "name",
                    PagingConfig(
                        pageSize = 2,
                        prefetchDistance = 2,
                        maxSize = 10,
                        jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
                    )
                )
            }
        }

        runBlocking {
            pager.flow.take(1).toList()
        }

        coVerify { meteoApi.places() }
        verify { placesDao.findAll("name%") }
        coVerify { placesDao.setAll(any()) }
        verify { repoPreferences setProperty "placesTimestamp" value more(timestamp) }
    }

    @Test
    fun findPlacesNoLoadIfLoaded() {
        val timestamp = Instant.now().minusSeconds(RepoConfig.placesExpirySeconds).plusSeconds(2)

        coEvery { meteoApi.places() } returns emptyList()
        every { placesDao.findAll(any()) } returns pagingSource
        every { repoPreferences.placesTimestamp } returns timestamp

        val pager = runBlocking {
            with(repo) {
                findPlaces(
                    "name",
                    PagingConfig(
                        pageSize = 2,
                        prefetchDistance = 2,
                        maxSize = 10,
                        jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
                    )
                )
            }
        }

        runBlocking {
            pager.flow.take(1).toList()
        }

        coVerify(exactly = 0) { meteoApi.places() }
        verify { placesDao.findAll("name%") }
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
        every { placesDao.findAll(any()) } returns pagingSource
        every { repoPreferences.placesTimestamp } returns timestamp

        val pager1 = runBlocking {
            with(repo) {
                findPlaces(
                    "name",
                    PagingConfig(
                        pageSize = 2,
                        prefetchDistance = 2,
                        maxSize = 10,
                        jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
                    )
                )
            }
        }

        runBlocking {
            pager1.flow.take(1).toList()
        }

        coVerify { meteoApi.places() }
        verify { placesDao.findAll("name%") }

        val pager2 = runBlocking {
            with(repo) {
                findPlaces(
                    "eman",
                    PagingConfig(
                        pageSize = 2,
                        prefetchDistance = 2,
                        maxSize = 10,
                        jumpThreshold = PagingSource.LoadResult.Page.COUNT_UNDEFINED
                    )
                )
            }
        }

        runBlocking {
            pager2.flow.take(1).toList()
        }

        coVerify(exactly = 1) { meteoApi.places() }
        verify { placesDao.findAll("eman%") }
    }
}
