package gj.meteoras.ui.places.compose

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import gj.meteoras.ext.compose.then
import gj.meteoras.ui.places.PlacesViewAction

@Composable
fun PlacesAction(
    action: PlacesViewAction?,
    snackbarHostState: SnackbarHostState
) {
    when (action) {
        is PlacesViewAction.ShowMessage -> LaunchedEffect(action) {
            snackbarHostState.showSnackbar(
                message = action.message,
                actionLabel = action.action,
            ).then {
                action.callback?.invoke()
            }
        }
    }
}