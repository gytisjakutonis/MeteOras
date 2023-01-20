package gj.meteoras.ui.forecast

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import gj.meteoras.data.Place
import kotlin.time.ExperimentalTime

object ForecastDestination {
    const val route = "forecast/{code}"
    private val code = navArgument("code") { type = NavType.StringType }

    @ExperimentalMaterial3Api
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalTime
    fun NavGraphBuilder.compose(navController: NavHostController) {
        composable(
            route = ForecastDestination.route,
            arguments = listOf(code)
        ) { backStackEntry ->
            ForecastScreen(
                navController = navController,
                code = backStackEntry.arguments?.getString(code.name) ?: ""
            )
        }
    }

    fun NavHostController.navigate(
        place: Place,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        navigate("forecast/${place.code}", builder)
    }
}
