package gj.meteoras.net

import gj.meteoras.net.api.MeteoApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val restModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(NetConfig.meteoUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(MeteoApi::class.java)
    }
}
