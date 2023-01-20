package gj.meteoras.ui.place

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import gj.meteoras.Updater
import gj.meteoras.ui.ScreenFold
import gj.meteoras.ui.theme.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.time.ExperimentalTime

@ExperimentalAnimationApi
@ExperimentalTime
@ExperimentalMaterial3Api
@Composable
fun PlaceScreen(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val model = get<PlaceModel>()
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val places = derivedStateOf { state.value?.places ?: emptyList() }
    val favourites = derivedStateOf { state.value?.favouritePlaces ?: emptyList() }
    val busy = derivedStateOf { state.value?.busy ?: false }
    val filter = derivedStateOf { state.value?.filter ?: "" }
    val updater = get<Updater>()
    val activity = LocalContext.current as Activity

    LaunchedEffect(model) {
        Firebase.analytics.logEvent("view_places", null)

        model.resume(scope)
    }

    ScreenFold(
        snackbarHostState = snackbarHostState,
    ) {
        PlaceFilter(
            value = filter.value,
            onValueChange = { value ->
                scope.launch { model.filter(value) }
            }
        )

        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            AnimatedVisibility(visible = !busy.value) {
                PlaceList(
                    items = places.value,
                    favourites = favourites.value,
                    onClick = { place ->
                        scope.launch { model.use(place) }
                    }
                )
            }

            AnimatedVisibility(visible = busy.value) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(Unit) {
        var snackJob: Job? = null

        scope.launch(Dispatchers.Default) {
            updater.check(activity)
        }

        model.actions.collect { action ->
            when (action) {
                is PlaceAction.ShowMessage -> {
                    snackJob?.cancel()
                    snackJob = launch {
                        snackbarHostState.show(action.state)
                    }
                }

                is PlaceAction.OpenPlace -> {
                    with (gj.meteoras.ui.forecast.ForecastDestination) {
                        navController.navigate(action.place) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}
