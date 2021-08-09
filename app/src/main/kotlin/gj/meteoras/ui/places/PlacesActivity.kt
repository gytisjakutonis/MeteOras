package gj.meteoras.ui.places

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
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
        Filter(state?.filter ?: "", model::filter)
    }

    @Composable
    fun Filter(
        value: String,
        onValueChange: (String) -> Unit
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray
            ),
            elevation = 1.dp,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = Color.LightGray
                )

                Spacer(modifier = Modifier.size(5.dp))

                Box(modifier = Modifier.weight(1f)) {
                    Row() {
                        AnimatedVisibility(
                            visible = value.isEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = "Search",
                                color = Color.LightGray
                            )
                        }
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        maxLines = 1,
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black),
                        cursorBrush = SolidColor(Color.DarkGray),
                    )
                }

                Spacer(modifier = Modifier.size(5.dp))

                AnimatedVisibility(
                    visible = value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        enabled = value.isNotEmpty(),
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = { onValueChange("") }
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = null,
                            tint = Color.LightGray,
                        )
                    }
                }
            }
        }
    }
}
