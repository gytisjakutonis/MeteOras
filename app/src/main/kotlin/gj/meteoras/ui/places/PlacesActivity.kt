package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesActivity : ComponentActivity() {

    val model: PlacesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = model.state.observeAsState()

            ViewState(state = state.value)
        }
    }

    @Composable
    fun ViewState(state: PlacesViewState?) {
        Filter(state?.filter, model::filter)
    }

    @Composable
    fun Filter(
        value: String?,
        onValueChange: (String) -> Unit
    ) {
        TextField(
            value = value ?: "",
            onValueChange = onValueChange
        )
    }
}
