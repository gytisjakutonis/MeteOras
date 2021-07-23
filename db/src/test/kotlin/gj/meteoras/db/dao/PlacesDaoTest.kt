package gj.meteoras.db.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import gj.meteoras.db.Database
import gj.meteoras.db.data.PlaceDb
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
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

@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class PlacesDaoTest : KoinTest{

    val db : Database by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(ApplicationProvider.getApplicationContext())

        modules(
            module {
                single {
                    Room.inMemoryDatabaseBuilder(
                        androidContext(),
                        Database::class.java,
                    ).build()
                }
            }
        )
    }

    @Before
    fun before() {
    }

    @After
    fun after() {
        db.close()
        unmockkAll()
    }

    @Test
    fun insertAll() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
                administrativeDivision = "administrativeDivision2"
            ),
            PlaceDb(
                code = "code3",
                name = "name3",
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
                countryCode = "countryCode1"
            ),
            PlaceDb(
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
            PlaceDb(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
            ),
            PlaceDb(
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
            PlaceDb(
                code = "code1",
                name = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "name1",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code3",
                name = "name3",
                countryCode = "countryCode3",
            ),
        )

        val placesAfter = listOf(
            PlaceDb(
                code = "code4",
                name = "name1",
                countryCode = "countryCode1"
            ),
            PlaceDb(
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

    @Test
    fun findAll() {
        val places = listOf(
            PlaceDb(
                code = "code1",
                name = "abc",
                countryCode = "countryCode1"
            ),
            PlaceDb(
                code = "code2",
                name = "abcd",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code3",
                name = "abcde",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code4",
                name = "fgh",
                countryCode = "countryCode1",
            ),
            PlaceDb(
                code = "code5",
                name = "fghi",
                countryCode = "countryCode1",
            ),
        )

        runBlocking {
            db.places().insertAll(places)
        }

        var placesDb = runBlocking {
            db.places().findAll("ab%").load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        }

        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
            assertThat(it.data.size).isEqualTo(3)
        }

        placesDb = runBlocking {
            db.places().findAll("abc").load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        }

        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
            assertThat(it.data.size).isEqualTo(1)
        }

        placesDb = runBlocking {
            db.places().findAll("%bc%").load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        }

        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
            assertThat(it.data.size).isEqualTo(3)
        }

        placesDb = runBlocking {
            db.places().findAll("%bc").load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        }

        assertThat(placesDb).isInstanceOfSatisfying(PagingSource.LoadResult.Page::class.java) {
            assertThat(it.data.size).isEqualTo(1)
        }
    }
}
