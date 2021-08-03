package gj.meteoras.net

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

val netModule = module {
    single {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .cache(Cache(File(get<Context>().cacheDir, "http"), NetConfig.cacheSizeBytes))
            .apply {
                addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
            .build()
    }
}
