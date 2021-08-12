package gj.meteoras.ui.places.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import gj.meteoras.ext.compose.AnimatedVisibility
import gj.meteoras.ui.theme.supplementary
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalAnimationApi
@Composable
fun PlacesFilter(
    value: String,
    onValueChange: (String) -> Unit
) {
    val state = rememberSaveable { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colors.supplementary
            )
            .padding(top = 5.dp, bottom = 5.dp)
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colors.supplementary,
            modifier = Modifier.padding(start = 5.dp)
        )

        Box(
            modifier = Modifier.weight(1f).padding(start = 5.dp, end = 5.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            AnimatedVisibility(visible = state.value.isEmpty(),) {
                Text(
                    text = "Type name here",
                    color = MaterialTheme.colors.supplementary,
                )
            }

            BasicTextField(
                value = state.value,
                onValueChange = { state.value = it },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
                modifier = Modifier.fillMaxWidth()
            )

            LaunchedEffect(value) {
                snapshotFlow { state.value }
                    .debounce(inputDelay)
                    .distinctUntilChanged()
                    .collect { onValueChange(it) }
            }
        }

        AnimatedVisibility(
            visible = state.value.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                enabled = state.value.isNotEmpty(),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .then(Modifier.size(24.dp)),
                onClick = { state.value = "" }
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colors.supplementary,
                )
            }
        }
    }
}

@ExperimentalTime
private val inputDelay = Duration.milliseconds(200L)
