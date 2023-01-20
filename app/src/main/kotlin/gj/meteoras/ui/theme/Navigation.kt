package gj.meteoras.ui.theme

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import gj.meteoras.ui.forecast.ForecastDestination
import gj.meteoras.ui.place.PlaceDestination
import kotlin.time.ExperimentalTime

@ExperimentalAnimationApi
@ExperimentalTime
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = PlaceDestination.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
    ) {
        with (PlaceDestination) {
            compose(navController)
        }
        with (ForecastDestination) {
            compose(navController)
        }
    }
}

private val exitTransition = slideOutHorizontally(
    targetOffsetX = { width -> -width },
    animationSpec = tween(durationMillis = 1000)
) + fadeOut(
    animationSpec = tween(durationMillis = 1000)
)

private val enterTransition = fadeIn(
    animationSpec = tween(durationMillis = 1000)
) + slideInHorizontally(
    initialOffsetX = { width -> width },
    animationSpec = tween(durationMillis = 1000)
)
