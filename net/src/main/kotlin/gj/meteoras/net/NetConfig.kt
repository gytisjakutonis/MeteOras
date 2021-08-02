package gj.meteoras.net

internal object NetConfig {
    val meteoUrl: String get() = BuildConfig.METEO_URL
    val cacheSizeBytes: Long get() = 1024 * 1024 * 10L
}
