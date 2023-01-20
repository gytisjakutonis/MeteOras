package gj.meteoras.ui.forecast

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
@ExperimentalFoundationApi
@Composable
fun ForecastScreen(
    navController: NavHostController,
    code: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val model = get<ForecastModel>()
    val state = model.state.collectAsState(null, Dispatchers.Default)
    val busy = derivedStateOf { state.value?.busy ?: false }
    val place = derivedStateOf { state.value?.place }
    val timestamps = derivedStateOf { state.value?.timestamps ?: emptyList() }

    LaunchedEffect(model) {
        Firebase.analytics.logEvent(
            "view_place",
            Bundle().apply {
                putString("code", code)
            }
        )

        model.resume(scope)
        model.use(code)
    }

    ScreenFold(
        snackbarHostState = snackbarHostState,
    ) {
        AnimatedVisibility(visible = place.value != null) {
            place.value?.let { PlaceHeader(it) }
        }

        AnimatedVisibility(visible = !busy.value) {
            place.value?.let {
                TimestampList(
                    place = it,
                    timestamps = timestamps.value,
                    onRefresh = {
                        scope.launch {
                            model.use(code)
                        }
                    }
                )
            }
        }

        AnimatedVisibility(visible = busy.value) {
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(Unit) {
        var snackJob: Job? = null

        model.actions.collect { action ->
            when (action) {
                is ForecastAction.ShowMessage -> {
                    snackJob?.cancel()
                    snackJob = launch {
                        snackbarHostState.show(action.state)
                    }
                }
            }
        }
    }
}
