package gj.meteoras.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gj.meteoras.data.Place
import gj.meteoras.db.Database
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

@ExperimentalCoroutinesApi
@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class PlacesDaoTest : KoinTest{

    // https://medium.com/@eyalg/testing-androidx-room-kotlin-coroutines-2d1faa3e674f
    // Room DAO transaction functions override threading, so does not work with runBlockingTest

    val db : Database by inject()

    // using this rule forces main thread for room, check other comments below
//    @get:Rule
//    val executorRule = InstantTaskExecutorRule()

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
//                        .allowMainThreadQueries()
//                        .setTransactionExecutor(dispatcher.asExecutor())
//                        .setQueryExecutor(dispatcher.asExecutor())
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
            Place(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            Place(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
            Place(
                code = "code3",
                name = "name3",
                countryCode = "countryCode3",
                administrativeDivision = "administrativeDivision2",
                coordinates = Place.Coordinates(
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
            Place(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            Place(
                code = "code1",
                name = "name1",
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
            Place(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            Place(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
            ),
            Place(
                code = "code3",
                name = "name3",
                countryCode = "countryCode3",
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(3)

        runBlocking {
            db.places().deleteAll()
        }

        placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(0)
    }

    @Test
    fun setAll() {
        val placesBefore = listOf(
            Place(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            Place(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
            ),
            Place(
                code = "code3",
                name = "name3",
                countryCode = "countryCode3",
            ),
        )

        val placesAfter = listOf(
            Place(
                code = "code4",
                name = "name1",
                countryCode = "countryCode1"
            ),
            Place(
                code = "code5",
                name = "name1",
                countryCode = "countryCode1",
            )
        )

        runBlocking {
            db.places().insertAll(placesBefore)
        }

        var placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(3)

        runBlocking {
            db.places().setAll(placesAfter)
        }

        placesCountDb = runBlocking {
            db.places().countAll()
        }

        assertThat(placesCountDb).isEqualTo(2)
    }

//    @Test
//    fun findAll() {
//        val places = listOf(
//            Place(
//                code = "code1",
//                name = "abc",
//                countryCode = "countryCode1"
//            ),
//            Place(
//                code = "code2",
//                name = "abcd",
//                countryCode = "countryCode1",
//            ),
//            Place(
//                code = "code3",
//                name = "abcde",
//                countryCode = "countryCode1",
//            ),
//            Place(
//                code = "code4",
//                name = "fgh",
//                countryCode = "countryCode1",
//            ),
//            Place(
//                code = "code5",
//                name = "fghi",
//                countryCode = "countryCode1",
//            ),
//        )
//
//        runBlocking {
//            db.places().insertAll(places)
//        }
//
//        var placesDb = runBlocking {
//            db.places().findAll("ab%").load(
//                PagingSource.LoadParams.Refresh(
//                    key = null,
//                    loadSize = 10,
//                    placeholdersEnabled = false
//                )
//            )
//        }
//
//        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
//            assertThat(it.data.size).isEqualTo(3)
//        }
//
//        placesDb = runBlocking {
//            db.places().findAll("abc").load(
//                PagingSource.LoadParams.Refresh(
//                    key = null,
//                    loadSize = 10,
//                    placeholdersEnabled = false
//                )
//            )
//        }
//
//        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
//            assertThat(it.data.size).isEqualTo(1)
//        }
//
//        placesDb = runBlocking {
//            db.places().findAll("%bc%").load(
//                PagingSource.LoadParams.Refresh(
//                    key = null,
//                    loadSize = 10,
//                    placeholdersEnabled = false
//                )
//            )
//        }
//
//        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
//            assertThat(it.data.size).isEqualTo(3)
//        }
//
//        placesDb = runBlocking {
//            db.places().findAll("%bc").load(
//                PagingSource.LoadParams.Refresh(
//                    key = null,
//                    loadSize = 10,
//                    placeholdersEnabled = false
//                )
//            )
//        }
//
//        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
//            assertThat(it.data.size).isEqualTo(1)
//        }
//    }
}
