package gj.meteoras.ui

import androidx.annotation.VisibleForTesting

@VisibleForTesting
internal object UiConfig {
    val preferencesName: String get() = "gj.meteoras.ui.preferences"
    val favouritesLimit: Int get() = 3
}
