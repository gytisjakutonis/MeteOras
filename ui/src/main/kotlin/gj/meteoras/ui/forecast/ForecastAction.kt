package gj.meteoras.ui.forecast

import gj.meteoras.ui.SnackbarState

sealed class ForecastAction {

    data class ShowMessage(
        val state: SnackbarState
    ) : ForecastAction()
}
