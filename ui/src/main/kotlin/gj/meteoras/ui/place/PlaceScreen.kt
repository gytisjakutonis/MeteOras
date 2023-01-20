package gj.meteoras.ui.place

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import gj.meteoras.ui.ScreenFold
import gj.meteoras.ui.forecast.ForecastDestination
import gj.meteoras.ui.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalAnimationApi::class, ExperimentalTime::class)
@Composable
fun PlaceScreen(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val model = get<PlaceModel>()
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val places = derivedStateOf { state.value?.places ?: emptyList() }
    val favourites = derivedStateOf { state.value?.favouritePlaces ?: emptyList() }
    val busy = derivedStateOf { state.value?.busy ?: false }
    val filter = derivedStateOf { state.value?.filter ?: "" }

    LaunchedEffect(model) {
        Firebase.analytics.logEvent("view_places", null)

        model.resume(scope)
    }

    ScreenFold(
        scaffoldState = scaffoldState,
    ) {
        PlaceFilter(
            value = filter.value,
            onValueChange = { value ->
                scope.launch { model.filter(value) }
            }
        )

        AnimatedContent(
            targetState = busy.value,
            transitionSpec = { progressTransform }
        ) { busy ->
            if (busy) {
                CircularProgressIndicator()
            } else {
                PlaceList(
                    items = places.value,
                    favourites = favourites.value,
                    onClick = { place ->
                        scope.launch { model.use(place) }
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        var snackJob: Job? = null

        model.actions.collect { action ->
            when (action) {
                is PlaceAction.ShowMessage -> {
                    snackJob?.cancel()
                    snackJob = launch {
                        scaffoldState.snackbarHostState.show(action.state)
                    }
                }

                is PlaceAction.OpenPlace -> {
                    with (ForecastDestination) {
                        navController.navigate(action.place) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
private val progressTransform = fadeIn(
    animationSpec = tween(durationMillis = 400)
) with fadeOut(
    animationSpec = tween(durationMillis = 400)
)
