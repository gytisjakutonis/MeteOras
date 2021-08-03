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
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .cache(Cache(get<Context>().cacheDir, NetConfig.cacheSizeBytes))
            .build()
    }
}
