package gj.meteoras.ui.places.compose

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import gj.meteoras.Updater
import gj.meteoras.ext.compose.collectAsAction
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.theme.paddings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun PlacesView(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
) {
    val model: PlacesViewModel = getViewModel()
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val action = model.action.collectAsAction(null)
    val scope = rememberCoroutineScope { Dispatchers.Default }
    val places = derivedStateOf { state.value?.places ?: emptyList() }
    val favourites = derivedStateOf {
        if (state.value?.filter.isNullOrEmpty()) {
            state.value?.favourites?.mapNotNull { code ->
                state.value?.places?.firstOrNull { it.code == code }
            } ?: emptyList()
        } else {
            emptyList()
        }
    }
    val filter = derivedStateOf { state.value?.filter ?: "" }

    Column(
        modifier = Modifier.padding(MaterialTheme.paddings.screen)
    ) {
        PlacesFilter(
            value = filter.value,
            onValueChange = { value ->
                scope.launch { model.filter(value) }
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.paddings.screen)
        ) {
            AnimatedVisibility(visible = state.value?.busy == false) {
                PlacesList(
                    items = places.value,
                    favourites = favourites.value,
                    onClick = { place ->
                        scope.launch { model.use(place) }
                    }
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

    val updater = get<Updater>()
    val activity = LocalContext.current as Activity

    LaunchedEffect(true) {
        Firebase.analytics.logEvent("view_places", null)
        scope.launch { model.resume() }

        scope.launch {
            updater.check(activity)
        }
    }
}
