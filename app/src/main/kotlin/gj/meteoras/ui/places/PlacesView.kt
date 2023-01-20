package gj.meteoras.ui.places.compose

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import gj.meteoras.Updater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun PlacesView(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope { Dispatchers.Default }

    val updater = get<Updater>()
    val activity = LocalContext.current as Activity

    LaunchedEffect(true) {
        scope.launch {
            updater.check(activity)
        }
    }
}
