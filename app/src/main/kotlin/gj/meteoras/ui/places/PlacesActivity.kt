package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ui.UiTheme
import gj.meteoras.ui.compose.TopNavigationBar
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class PlacesActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = model.state.observeAsState()

            UiTheme {
                val systemUiController = rememberSystemUiController()
                val systemBarColor = MaterialTheme.colors.primaryVariant

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = systemBarColor,
                    )
                }

                Scaffold(
                    topBar = {
                        TopNavigationBar("Find a Place")
                    },
                    content = {
                        ViewState(state = state.value)
                    }
                )
            }
        }
    }

    @Composable
    private fun ViewState(state: PlacesViewState?) {
        Column {
            PlacesFilter(model::filter)
        }
    }
}

//                val infiniteTransition = rememberInfiniteTransition()
//                val angle by infiniteTransition.animateFloat(
//                    initialValue = 0f,
//                    targetValue = 90f,
//                    animationSpec = infiniteRepeatable(
//                        animation = keyframes { durationMillis = 1000 },
//                        repeatMode = RepeatMode.Reverse
//                    )
//                )
