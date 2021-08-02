package gj.meteoras.repo

import androidx.annotation.VisibleForTesting

@VisibleForTesting
internal object RepoConfig {
    val preferencesName: String get() = "gj.meteoras.repo.preferences"
    val placesExpirySeconds: Long get() = 60 * 60 * 24L
}
