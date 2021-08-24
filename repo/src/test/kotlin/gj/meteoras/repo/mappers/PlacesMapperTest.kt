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
    fun toDaoNullIfEmpty() {
        val net = PlaceNet(
            code = null,
            name = null,
            administrativeDivision = null,
            countryCode = null,
            coordinates = null,
            country = null
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoNullIfNullName() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoNullIfNullCode() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoNullIfNullCountryCode() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoNullIfNullCountry() {
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

        val dao = net.toDao()

        assertThat(dao).isNotNull
    }

    @Test
    fun toDaoIfNullDivision() {
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

        val dao = net.toDao()

        assertThat(dao).isNotNull
    }

    @Test
    fun toDaoIfNullCoordinates() {
        val net = PlaceNet(
            code = "code",
            name = "name",
            administrativeDivision = "division",
            countryCode = "cc",
            coordinates = null,
            country = "Country"
        )

        val dao = net.toDao()

        assertThat(dao).isNotNull
    }

    @Test
    fun toDaoNullIfNullLatitude() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoNullIfNullLongitude() {
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

        val dao = net.toDao()

        assertThat(dao).isNull()
    }

    @Test
    fun toDaoValues() {
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

        val dao = net.toDao()

        assertThat(dao?.code).isEqualTo("code")
        assertThat(dao?.name).isEqualTo("name")
        assertThat(dao?.administrativeDivision).isEqualTo("division")
        assertThat(dao?.countryCode).isEqualTo("cc")
        assertThat(dao?.country).isEqualTo("Country")
        assertThat(dao?.coordinates?.latitude).isEqualTo(12.34)
        assertThat(dao?.coordinates?.longitude).isEqualTo(56.78)
    }

}
