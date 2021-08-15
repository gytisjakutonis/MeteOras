package gj.meteoras.net.api

import gj.meteoras.net.data.ForecastNet
import gj.meteoras.net.data.PlaceNet
import retrofit2.http.GET
import retrofit2.http.Path

interface MeteoApi {

    @GET("places")
    suspend fun places(): List<PlaceNet>

    @GET("places/{code}")
    suspend fun place(@Path("code") code: String): PlaceNet

    @GET("places/{code}/forecasts/long-term")
    suspend fun forecast(@Path("code") code: String): ForecastNet
}
