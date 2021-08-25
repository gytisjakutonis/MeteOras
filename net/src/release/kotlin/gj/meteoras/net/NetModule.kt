package gj.meteoras.net

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
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
}
