package gj.meteoras.ui.theme

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlin.time.ExperimentalTime

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalTime
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = gj.meteoras.ui.place.PlaceDestination.route,
    ) {
        with (gj.meteoras.ui.place.PlaceDestination) {
            compose(navController)
        }
        with (gj.meteoras.ui.forecast.ForecastDestination) {
            compose(navController)
        }
    }
}
