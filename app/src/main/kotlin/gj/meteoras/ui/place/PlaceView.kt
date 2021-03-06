package gj.meteoras.ui.places.compose

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import gj.meteoras.ext.compose.collectAsAction
import gj.meteoras.ui.place.PlaceViewModel
import gj.meteoras.ui.place.compose.PlaceAction
import gj.meteoras.ui.place.compose.TimestampsList
import gj.meteoras.ui.theme.paddings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalFoundationApi
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
    val action = model.action.collectAsAction(null)
    val scope = rememberCoroutineScope { Dispatchers.Default }
    val forecast = derivedStateOf { state.value?.forecast }
    val place = derivedStateOf { forecast.value?.place }
    val timestamps = derivedStateOf { forecast.value?.timestamps ?: emptyList() }

    Column(
        modifier = Modifier
            .padding(MaterialTheme.paddings.screen)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            AnimatedVisibility(visible = place.value != null) {
                PlaceHeader(place.value)
            }

            AnimatedVisibility(visible = state.value?.busy == false) {
                TimestampsList(
                    place = place.value,
                    timestamps = timestamps.value,
                    onRefresh = {
                        scope.launch {
                            model.resume(code)
                        }
                    }
                )
            }
        }

        AnimatedVisibility(visible = state.value?.busy == true) {
            CircularProgressIndicator()
        }
    }

    PlaceAction(
        action = action.value,
        snackbarHostState = scaffoldState.snackbarHostState,
        navController = navController
    )

    LaunchedEffect(true) {
        Firebase.analytics.logEvent(
            "view_place",
            Bundle().apply {
                putString("code", code)
            }
        )
        scope.launch { model.resume(code) }
    }
}
