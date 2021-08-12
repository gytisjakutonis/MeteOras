package gj.meteoras.ui.places.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import gj.meteoras.ext.compose.AnimatedVisibility
import gj.meteoras.ext.compose.collectAsAction
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.theme.paddings
import kotlinx.coroutines.Dispatchers
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun PlacesView(
    model: PlacesViewModel,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
) {
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val action = model.action.collectAsAction(null)

    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
    ) {
        PlacesFilter(
            value = state.value?.filter ?: "",
            onValueChange = model::filter
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.paddings.screenPadding),
        ) {
            AnimatedVisibility(visible = state.value?.busy == false) {
                PlacesList(
                    items = state.value?.places ?: emptyList(),
                    onClick = model::use
                )
            }

            AnimatedVisibility(visible = state.value?.busy == true) {
                CircularProgressIndicator()
            }
        }
    }

    PlacesAction(
        action = action.value,
        snackbarHostState = scaffoldState.snackbarHostState,
        navController = navController
    )

    LaunchedEffect(true) {
        model.resume()
    }
}
