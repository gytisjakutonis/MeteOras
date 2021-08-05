package gj.meteoras.repo

import androidx.annotation.VisibleForTesting
import java.time.Duration

@VisibleForTesting
internal object RepoConfig {
    val preferencesName: String get() = "gj.meteoras.repo.preferences"
    val placesTimeout: Duration get() = Duration.ofSeconds(10L)//60 * 60 * 24L)
    val apiTimeout: Duration get() = Duration.ofSeconds(20L)
}
