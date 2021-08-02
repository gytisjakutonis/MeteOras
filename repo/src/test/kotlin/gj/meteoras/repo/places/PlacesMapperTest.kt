package gj.meteoras.repo.places

import gj.meteoras.net.data.PlaceNet
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlacesMapperTest {

    @Test
    fun toDaoNullIfEmpty() {
        val net = PlaceNet(
            code = null,
            name = null,
            administrativeDivision = null,
            countryCode = null,
            coordinates = null
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
            )
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
            )
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
            countryCode = null,
            coordinates = PlaceNet.Coordinates(
                latitude = 12.34,
                longitude = 56.78
            )
        )

        val dao = net.toDao()

        assertThat(dao).isNull()
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
            )
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
            coordinates = null
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
            )
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
            )
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
            )
        )

        val dao = net.toDao()

        assertThat(dao?.code).isEqualTo("code")
        assertThat(dao?.name).isEqualTo("name")
        assertThat(dao?.administrativeDivision).isEqualTo("division")
        assertThat(dao?.countryCode).isEqualTo("cc")
        assertThat(dao?.coordinates?.latitude).isEqualTo(12.34)
        assertThat(dao?.coordinates?.longitude).isEqualTo(56.78)
    }

}
