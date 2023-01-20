package gj.meteoras.ui.place

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlin.time.ExperimentalTime

object PlaceDestination {
    const val route = "place"

    @ExperimentalAnimationApi
    @ExperimentalTime
    fun NavGraphBuilder.compose(navController: NavHostController) {
        composable(this@PlaceDestination.route) {
            PlaceScreen(
                navController = navController,
            )
        }
    }
}
