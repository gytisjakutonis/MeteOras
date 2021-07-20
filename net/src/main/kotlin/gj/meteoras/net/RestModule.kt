package gj.meteoras.net

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val restModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.METEO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(Meteo::class.java)
    }
}
