package gj.meteoras.ui.places.compose

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import gj.meteoras.ext.compose.showSnackbar
import gj.meteoras.ui.place.PlaceDestination
import gj.meteoras.ui.places.PlacesViewAction

@Composable
fun PlacesAction(
    action: PlacesViewAction?,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController
) {
    when (action) {
        is PlacesViewAction.ShowMessage -> LaunchedEffect(action) {
            snackbarHostState.showSnackbar(
                message = action.message,
                action = action.action,
                callback = action.callback
            )
        }

        is PlacesViewAction.OpenPlace -> LaunchedEffect(action) {
            with (PlaceDestination) {
                navController.navigate(action.place) {
                    launchSingleTop = true
                }
            }
        }
    }
}
