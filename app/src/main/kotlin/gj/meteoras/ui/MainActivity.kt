package gj.meteoras.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ui.compose.TopNavigationBar
import gj.meteoras.ui.places.PlacesViewModel
import gj.meteoras.ui.places.compose.PlacesView
import gj.meteoras.ui.theme.Theme
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scaffoldState = rememberScaffoldState()

            Theme {
                val systemUiController = rememberSystemUiController()
                val systemBarColor = MaterialTheme.colors.primaryVariant

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = systemBarColor,
                    )
                }

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopNavigationBar("Find a Place")
                    }
                ) {
                    PlacesView(
                        model = model,
                        scaffoldState.snackbarHostState
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        model.resume()
    }
}
