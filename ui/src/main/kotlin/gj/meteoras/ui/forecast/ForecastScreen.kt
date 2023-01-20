package gj.meteoras.ui.forecast

import android.os.Bundle
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
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
import gj.meteoras.ui.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.time.ExperimentalTime

@ExperimentalAnimationApi
@ExperimentalTime
@ExperimentalFoundationApi
@Composable
fun ForecastScreen(
    navController: NavHostController,
    code: String
) {
    val scaffoldState = rememberScaffoldState()
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
        scaffoldState = scaffoldState,
    ) {
        AnimatedContent(
            targetState = busy.value,
            transitionSpec = { progressTransform }
        ) { busy ->
            if (busy) {
                CircularProgressIndicator()
            } else place.value?.let {
                Column {
                    PlaceHeader(it)

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
        }
    }

    LaunchedEffect(Unit) {
        var snackJob: Job? = null

        model.actions.collect { action ->
            when (action) {
                is ForecastAction.ShowMessage -> {
                    snackJob?.cancel()
                    snackJob = launch {
                        scaffoldState.snackbarHostState.show(action.state)
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
