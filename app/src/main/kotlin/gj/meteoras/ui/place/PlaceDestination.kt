package gj.meteoras.ui.place

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlin.time.ExperimentalTime

object PlaceDestination {
    const val route = "place"

    @ExperimentalMaterial3Api
    @ExperimentalAnimationApi
    @ExperimentalTime
    fun NavGraphBuilder.compose(navController: NavHostController) {
        composable(PlaceDestination.route) {
            PlaceScreen(
                navController = navController,
            )
        }
    }
}
