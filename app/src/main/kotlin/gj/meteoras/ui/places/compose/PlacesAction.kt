package gj.meteoras.ui.places.compose

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import gj.meteoras.ext.compose.showSnackbar
import gj.meteoras.ui.place.PlaceDestination
import gj.meteoras.ui.places.PlacesViewAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PlacesAction(
    action: PlacesViewAction?,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope { Dispatchers.Default }

    when (action) {
        is PlacesViewAction.ShowMessage -> LaunchedEffect(action) {
            snackbarHostState.showSnackbar(
                message = action.message,
                action = action.action,
                callback = action.callback?.let { callback ->
                    { scope.launch { callback() } }
                }
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
