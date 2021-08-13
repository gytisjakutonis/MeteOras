package gj.meteoras.repo.places

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gj.meteoras.db.Database
import gj.meteoras.net.apiModule
import gj.meteoras.repo.RepoConfig
import gj.meteoras.repo.RepoPreferences
import gj.meteoras.test.TimberRule
import gj.meteoras.test.trustSsl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.annotation.Config
import timber.log.Timber
import java.time.Duration
import java.time.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class PlacesRepoIntegartionTest : KoinTest {

    @MockK
    private lateinit var repoPreferences: RepoPreferences

    val db : Database by inject()

    val repo : PlacesRepo by inject()

    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val timberRule = TimberRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(ApplicationProvider.getApplicationContext())

        modules(
            module {
                single {
                    OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .callTimeout(Duration.ofSeconds(5L))
                        .connectTimeout(Duration.ofSeconds(5L))
                        .readTimeout(Duration.ofSeconds(5L))
                        .writeTimeout(Duration.ofSeconds(5L))
                        .trustSsl()
                        .build()
                }
            },
            apiModule,
            module {
                single {
                    Room.inMemoryDatabaseBuilder(
                        androidContext(),
                        Database::class.java,
                    ).build()
                }
                single { get<Database>().places() }
                single { repoPreferences }
                single { PlacesRepo(get(), get(), get()) }
            }
        )
    }

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        Timber.plant(Timber.DebugTree())
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
    fun syncPlaces() {
        val timestamp = Instant.ofEpochSecond(0L)
        every { repoPreferences.placesTimestamp } returns timestamp

        val result = runBlocking {
            repo.syncPlaces()
        }

        assertThat(result.isSuccess).isTrue
    }

    @Test
    fun getPlace() {
        Timber.d("TEST")

//        val result = runBlocking {
//            repo.getPlace("code")
//        }
//
//        assertThat(result.isSuccess).isTrue
//        assertThat(result.getOrNull()?.name).isEqualTo("Vilnius")
//        assertThat(result.getOrNull()?.country).isEqualTo("Lietuva")
    }
}

