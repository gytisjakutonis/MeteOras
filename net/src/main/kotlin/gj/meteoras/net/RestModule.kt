package gj.meteoras.net

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun restModule(meteoUrl: String) = module {
    single {
        Retrofit.Builder()
            .baseUrl(meteoUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(Meteo::class.java)
    }
}
