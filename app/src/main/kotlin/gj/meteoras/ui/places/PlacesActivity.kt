package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import gj.meteoras.ui.UiTheme
import gj.meteoras.ui.compose.Fading
import gj.meteoras.ui.compose.TopNavigationBar
import gj.meteoras.ui.paddings
import gj.meteoras.ui.places.compose.PlacesList
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
        Column(
            modifier = Modifier.padding(MaterialTheme.paddings.screenPadding)
        ) {
            PlacesFilter(model::filter)

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.paddings.screenPadding),
            ) {
                PlacesList(state?.places ?: emptyList())

                Fading(visible = state?.busy == true) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
