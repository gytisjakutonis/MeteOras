package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import gj.meteoras.ui.UiTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class PlacesActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = model.state.observeAsState()

            UiTheme {
                ViewState(state = state.value)
            }
        }
    }

    @Composable
    fun ViewState(state: PlacesViewState?) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column {
                PlacesFilter(model::filter)
            }
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
