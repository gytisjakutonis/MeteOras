package gj.meteoras.net

import gj.meteoras.net.data.Place
import retrofit2.http.GET

interface Meteo {

    @GET("places")
    suspend fun places(): List<Place>
}
