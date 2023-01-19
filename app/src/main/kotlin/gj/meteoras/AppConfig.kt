package gj.meteoras

import java.time.Duration

internal object AppConfig {
    val updateTimeout: Duration get() = Duration.ofHours(1L)
}
