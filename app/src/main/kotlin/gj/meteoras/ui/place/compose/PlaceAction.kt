package gj.meteoras.ui.place.compose

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import gj.meteoras.ext.compose.showSnackbar
import gj.meteoras.ui.place.PlaceViewAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PlaceAction(
    action: PlaceViewAction?,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope { Dispatchers.Default }

    when (action) {
        is PlaceViewAction.ShowMessage -> LaunchedEffect(action) {
            snackbarHostState.showSnackbar(
                message = action.message,
                action = action.action,
                callback = action.callback?.let { callback ->
                    { scope.launch { callback() } }
                }
            )
        }

        is PlaceViewAction.GoBack -> LaunchedEffect(action) {
            navController.navigateUp()
        }
    }
}
