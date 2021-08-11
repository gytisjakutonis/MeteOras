package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ext.compose.collectAsAction
import gj.meteoras.ui.UiTheme
import gj.meteoras.ui.compose.TopNavigationBar
import gj.meteoras.ui.places.compose.PlacesAction
import gj.meteoras.ui.places.compose.PlacesView
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = model.state.collectAsState(null, Dispatchers.Default)
            val action = model.action.collectAsAction(null)
            val scaffoldState = rememberScaffoldState()

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
