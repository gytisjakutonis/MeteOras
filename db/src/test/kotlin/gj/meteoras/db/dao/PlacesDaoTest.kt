package gj.meteoras.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gj.meteoras.db.Database
import gj.meteoras.db.data.PlaceDb
import io.mockk.unmockkAll
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
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.annotation.Config
import kotlin.test.fail

@ExperimentalCoroutinesApi
@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class PlacesDaoTest : KoinTest {

    // https://medium.com/@eyalg/testing-androidx-room-kotlin-coroutines-2d1faa3e674f
    // Room DAO transaction functions override threading, so does not work with runBlockingTest

    val db : Database by inject()

    // using this rule forces main thread for room, check other comments below
    //@get:Rule
    //val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(ApplicationProvider.getApplicationContext())

        modules(
            module {
                single {
                    Room.inMemoryDatabaseBuilder(
                        androidContext(),
                        Database::class.java,
                    )
                        // using main thread blocks runBlocking
                        //.allowMainThreadQueries()
                        //.setTransactionExecutor(dispatcher.asExecutor())
                        //.setQueryExecutor(dispatcher.asExecutor())
                        .build()
                }
            }
        )
    }

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        db.close()
        unmockkAll()
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertAll() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1",
                normalisedName = "name1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
            PlaceDb(
                code = "code3",
                name = "name3",
                normalisedName = "name3",
                countryCode = "countryCode3",
                administrativeDivision = "administrativeDivision2",
                coordinates = PlaceDb.Coordinates(
                    latitude = 1.1,
                    longitude = 2.2
                )
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        val placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(3)
    }

    @Test
    fun uniqueCode() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1",
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        val placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(1)
    }

    @Test
    fun deleteAll() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code3",
                name = "name3",
                normalisedName = "name3",
                countryCode = "countryCode3",
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var placesCount = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCount).isEqualTo(3)

        runBlocking {
            db.places().deleteAll()
        }

        placesCount = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCount).isEqualTo(0)
    }

    @Test
    fun setAll() {
        val placesBefore = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code3",
                name = "name3",
                normalisedName = "name3",
                countryCode = "countryCode3",
            ),
        )

        val placesAfter = listOf(
            PlaceDb(
                code = "code4",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code5",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1",
            )
        )

        runBlocking {
            db.places().setAll(placesBefore)
        }

        var placesCount = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCount).isEqualTo(3)

        runBlocking {
            db.places().setAll(placesAfter)
        }

        placesCount = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCount).isEqualTo(2)
    }

    @Test
    fun findByName() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "abc",
                normalisedName = "abc",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "abcd",
                normalisedName = "abcd",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code3",
                name = "abcde",
                normalisedName = "abcde",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code4",
                name = "fgh",
                normalisedName = "fgh",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code5",
                name = "fghi",
                normalisedName = "fghi",
                countryCode = "countryCode1",
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var placesDb = runBlocking {
            db.places().findByName("ab%")
        }

        assertThat(placesDb.size).isEqualTo(3)

        placesDb = runBlocking {
            db.places().findByName("abc")
        }

        assertThat(placesDb.size).isEqualTo(1)

        placesDb = runBlocking {
            db.places().findByName("%bc%")
        }

        assertThat(placesDb.size).isEqualTo(3)

        placesDb = runBlocking {
            db.places().findByName("%bc")
        }

        assertThat(placesDb.size).isEqualTo(1)
    }

    @Test
    fun update() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name2",
                normalisedName = "name2",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var dbPlaces = runBlocking {
            db.places().findByName("name2")
        }

        runBlocking {
            db.places().update(dbPlaces[0].copy(normalisedName = "name22"))
        }

        dbPlaces = runBlocking {
            db.places().findByName("name22")
        }

        assertThat(dbPlaces.isNotEmpty()).isTrue
    }

    @Test
    fun findByCode() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name2",
                normalisedName = "name2",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var place = runBlocking {
            db.places().findByCode("code1")
        }

        assertThat(place?.name).isEqualTo("name1")

        place = runBlocking {
            db.places().findByCode("code3")
        }

        assertThat(place).isNull()
    }

    @Test
    fun delete() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                normalisedName = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name2",
                normalisedName = "name2",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        val place = runBlocking {
            db.places().findByCode("code1")
        } ?: fail()

        runBlocking {
            db.places().delete(place)
        }

        val result = runBlocking {
            db.places().findByCode("code1")
        }

        assertThat(result).isNull()
    }
}
