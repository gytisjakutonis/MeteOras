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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import gj.meteoras.ext.compose.AnimatedVisibility
import gj.meteoras.ui.place.PlaceViewModel
import gj.meteoras.ui.theme.paddings
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalAnimationApi
@ExperimentalTime
@Composable
fun PlaceView(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    code: String,
) {
    val model: PlaceViewModel = getViewModel()
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val place = derivedStateOf { state.value?.place }

    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(visible = state.value?.busy == false) {
                PlaceHeader(place.value)
            }

            AnimatedVisibility(visible = state.value?.busy == true) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(true) {
        model.resume(code)
    }
}
