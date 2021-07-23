package gj.meteoras.net.api

import gj.meteoras.net.data.PlaceNet
import retrofit2.http.GET

interface MeteoApi {

    @GET("places")
    suspend fun places(): List<PlaceNet>
}
