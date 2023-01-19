package gj.meteoras.net

import android.content.Context
import gj.meteoras.net.api.MeteoApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File

val netModule = module {
    single {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .callTimeout(NetConfig.httpCallTimeout)
            .connectTimeout(NetConfig.httpTimeout)
            .readTimeout(NetConfig.httpTimeout)
            .writeTimeout(NetConfig.httpTimeout)
            .cache(Cache(File(get<Context>().cacheDir, "http"), NetConfig.cacheSizeBytes))
            .apply {
                addNetworkInterceptor(
                    HttpLoggingInterceptor(
                        logger = { Timber.d(it) }
                    ).apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
            }
            .build()
    }

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
