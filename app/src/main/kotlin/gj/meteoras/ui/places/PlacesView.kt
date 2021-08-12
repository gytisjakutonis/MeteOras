package gj.meteoras.ui.places.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    snackbarHostState: SnackbarHostState,
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
                PlacesList(state.value?.places ?: emptyList())
            }

            AnimatedVisibility(visible = state.value?.busy == true) {
                CircularProgressIndicator()
            }
        }
    }

    PlacesAction(
        action = action.value,
        snackbarHostState = snackbarHostState
    )
}
