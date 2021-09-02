package gj.meteoras

import androidx.annotation.VisibleForTesting
import java.time.Duration

@VisibleForTesting
internal object AppConfig {
    val updateTimeout: Duration get() = Duration.ofHours(1L)
}
