package gj.meteoras.net

import gj.meteoras.data.Place
import retrofit2.http.GET

interface Meteo {

    @GET("places")
    suspend fun places(): List<Place>
}
