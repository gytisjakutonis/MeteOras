package gj.meteoras.ui.place

import gj.meteoras.data.Place
import gj.meteoras.ui.theme.SnackbarState

sealed class PlaceAction {

    data class ShowMessage(
        val state: SnackbarState
    ) : PlaceAction()

    data class OpenPlace(
        val place: Place
    ) : PlaceAction()
}
