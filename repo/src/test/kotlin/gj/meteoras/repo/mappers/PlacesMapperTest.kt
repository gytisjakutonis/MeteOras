package gj.meteoras.repo.mappers

import gj.meteoras.ext.lang.normalise
import gj.meteoras.net.data.PlaceNet
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class PlacesMapperTest {

    @Before
    fun before() {
        mockkStatic(String::normalise)
        every { any<String>().normalise() } returns "normalised"
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun toDataNullIfEmpty() {
        val net = PlaceNet(
            code = null,
            name = null,
            administrativeDivision = null,
            countryCode = null,
            coordinates = null,
            country = null
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataNullIfNullName() {
        val net = PlaceNet(
            code = "code",
            name = null,
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataNullIfNullCode() {
        val net = PlaceNet(
            code = null,
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataNullIfNullCountryCode() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = null,
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataNullIfNullCountry() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = null
        )

        val data = net.toData()
        assertThat(data).isNotNull

        val db = net.toDb()
        assertThat(db).isNotNull
    }

    @Test
    fun toDataIfNullDivision() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = null,
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNotNull

        val db = net.toDb()
        assertThat(db).isNotNull
    }

    @Test
    fun toDataIfNullCoordinates() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = null,
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNotNull

        val db = net.toDb()
        assertThat(db).isNotNull
    }

    @Test
    fun toDataNullIfNullLatitude() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = null,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataNullIfNullLongitude() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = null
            ),
            country = "Country"
        )

        val data = net.toData()
        assertThat(data).isNull()

        val db = net.toDb()
        assertThat(db).isNull()
    }

    @Test
    fun toDataValues() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            ),
            country = "Country"
        )

        val data = net.toData()

        assertThat(data?.code).isEqualTo("code")
        assertThat(data?.name).isEqualTo("name")
        assertThat(data?.administrativeDivision).isEqualTo("division")
        assertThat(data?.countryCode).isEqualTo("cc")
        assertThat(data?.country).isEqualTo("Country")
        assertThat(data?.coordinates?.latitude).isEqualTo(12.34)
        assertThat(data?.coordinates?.longitude).isEqualTo(56.78)

        val db = net.toDb()

        assertThat(db?.code).isEqualTo("code")
        assertThat(db?.name).isEqualTo("name")
        assertThat(db?.administrativeDivision).isEqualTo("division")
        assertThat(db?.countryCode).isEqualTo("cc")
        assertThat(db?.country).isEqualTo("Country")
        assertThat(db?.coordinates?.latitude).isEqualTo(12.34)
        assertThat(db?.coordinates?.longitude).isEqualTo(56.78)
    }
}
