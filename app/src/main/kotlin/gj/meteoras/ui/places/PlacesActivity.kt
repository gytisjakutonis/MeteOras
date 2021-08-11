package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ui.UiTheme
import gj.meteoras.ui.compose.TopNavigationBar
import gj.meteoras.ui.places.compose.PlacesAction
import gj.meteoras.ui.places.compose.PlacesView
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
class PlacesActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = model.state.observeAsState()
            val scaffoldState = rememberScaffoldState()
            val action = model.action.collectAsState(null, Dispatchers.Default)

            UiTheme {
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
                        state = state.value,
                        onFilter = model::filter
                    )

                    PlacesAction(
                        action = action.value,
                        snackbarHostState = scaffoldState.snackbarHostState
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
