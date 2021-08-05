package gj.meteoras.ui.places

sealed class PlacesViewAction {

    object None : PlacesViewAction()

    data class ShowMessage(
        val message: String,
        val action: String,
        val callback: () -> Unit
    ) : PlacesViewAction()
}
