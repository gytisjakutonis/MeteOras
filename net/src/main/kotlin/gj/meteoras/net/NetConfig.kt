package gj.meteoras.net

import java.time.Duration

internal object NetConfig {
    val meteoUrl: String get() = BuildConfig.METEO_URL
    val cacheSizeBytes: Long get() = 1024 * 1024 * 10L
    val httpCallTimeout: Duration get() = Duration.ofSeconds(10L)
    val httpTimeout: Duration get() = Duration.ofSeconds(5L)
}
