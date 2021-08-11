package gj.meteoras.ui.places

import java.time.Instant

sealed class PlacesViewAction {
    abstract val timestamp: Instant

    data class ShowMessage(
        override val timestamp: Instant = Instant.now(),
        val message: String,
        val action: String? = null,
        val callback: (() -> Unit)? = null
    ) : PlacesViewAction()
}
