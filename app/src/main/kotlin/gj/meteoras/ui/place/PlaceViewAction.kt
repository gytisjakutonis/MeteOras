package gj.meteoras.ui.place

import java.time.Instant

sealed class PlaceViewAction {
    abstract val timestamp: Instant

    data class ShowMessage(
        override val timestamp: Instant = Instant.now(),
        val message: String,
        val action: String? = null,
        val callback: (suspend () -> Unit)? = null
    ) : PlaceViewAction()

    object GoBack : PlaceViewAction() {
        override val timestamp: Instant = Instant.now()
    }
}
