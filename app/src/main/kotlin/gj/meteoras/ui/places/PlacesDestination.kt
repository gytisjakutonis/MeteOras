package gj.meteoras.ui.places

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import gj.meteoras.ui.places.compose.PlacesView
import kotlin.time.ExperimentalTime

object PlacesDestination {
    const val route: String = "places"

    @ExperimentalAnimationApi
    @ExperimentalTime
    fun NavGraphBuilder.build(
        scaffoldState: ScaffoldState,
        navController: NavHostController
    ) {
        composable(this@PlacesDestination.route) {
            PlacesView(
                scaffoldState = scaffoldState,
                navController = navController,
            )
        }
    }
}
