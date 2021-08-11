package gj.meteoras.ui.places

import java.time.Instant

sealed class PlacesViewAction(
    val timestamp: Instant = Instant.now()
) {

    data class ShowMessage(
        val message: String,
        val action: String? = null,
        val callback: (() -> Unit)? = null
    ) : PlacesViewAction()
}
