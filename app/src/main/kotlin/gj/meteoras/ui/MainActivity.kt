package gj.meteoras.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ui.place.PlaceDestination
import gj.meteoras.ui.places.PlacesDestination
import gj.meteoras.ui.theme.Theme
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            Theme {
                val systemUiController = rememberSystemUiController()
                val systemBarColor = MaterialTheme.colors.primaryVariant

                SideEffect {
                    systemUiController.setSystemBarsColor(color = systemBarColor,)
                }

                Scaffold(scaffoldState = scaffoldState,) {
                    NavHost(
                        navController = navController,
                        startDestination = PlacesDestination.route
                    ) {
                        with (PlacesDestination) {
                            build(
                                scaffoldState = scaffoldState,
                                navController = navController,
                            )
                        }

                        with (PlaceDestination) {
                            build(
                                scaffoldState = scaffoldState,
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}
