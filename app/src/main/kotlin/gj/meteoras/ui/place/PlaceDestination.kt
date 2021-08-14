package gj.meteoras.ui.place

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import gj.meteoras.ui.places.compose.PlaceView
import kotlin.time.ExperimentalTime

object PlaceDestination {
    const val route: String = "place/{code}"

    private val code = navArgument("code") { type = NavType.StringType }

    @ExperimentalTime
    @ExperimentalAnimationApi
    fun NavGraphBuilder.build(
        scaffoldState: ScaffoldState,
        navController: NavHostController
    ) {
        composable(
            route = this@PlaceDestination.route,
            arguments = listOf(code)
        ) { backStackEntry ->
            PlaceView(
                scaffoldState = scaffoldState,
                navController = navController,
                code = backStackEntry.arguments?.getString(code.name) ?: ""
            )
        }
    }

    fun NavHostController.navigate(
        place: gj.meteoras.data.Place,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        navigate("place/${place.code}", builder)
    }
}
