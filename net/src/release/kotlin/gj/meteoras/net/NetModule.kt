package gj.meteoras.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val netModule = module {
    single {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .callTimeout(NetConfig.httpCallTimeout)
            .connectTimeout(NetConfig.httpTimeout)
            .readTimeout(NetConfig.httpTimeout)
            .writeTimeout(NetConfig.httpTimeout)
            .cache(Cache(get<Context>().cacheDir, NetConfig.cacheSizeBytes))
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
}
