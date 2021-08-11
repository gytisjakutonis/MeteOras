package gj.meteoras.ui.places.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gj.meteoras.ui.compose.AnimatedVisibility
import gj.meteoras.ui.paddings
import gj.meteoras.ui.places.PlacesViewState

@ExperimentalAnimationApi
@Composable
fun PlacesView(
    state: PlacesViewState?,
    onFilter: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
    ) {
        PlacesFilter(onFilter)

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.paddings.screenPadding),
        ) {
            AnimatedVisibility(visible = state?.busy == false) {
                PlacesList(state?.places ?: emptyList())
            }

            AnimatedVisibility(visible = state?.busy == true) {
                CircularProgressIndicator()
            }
        }
    }
}
