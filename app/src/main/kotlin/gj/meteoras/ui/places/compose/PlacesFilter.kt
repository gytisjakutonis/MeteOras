package gj.meteoras.ui.places

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import gj.meteoras.ui.compose.Fading
import gj.meteoras.ui.supplementary

@ExperimentalAnimationApi
@Composable
fun PlacesFilter(
    onValueChange: (String) -> Unit
) {
    val value = rememberSaveable { mutableStateOf("") }

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
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colors.supplementary,
            modifier = Modifier.padding(start = 5.dp)
        )

        Spacer(modifier = Modifier.size(5.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Fading(visible = value.value.isEmpty(),) {
                Text(
                    text = "Type name here",
                    color = MaterialTheme.colors.supplementary,
                )
            }

            BasicTextField(
                value = value.value,
                onValueChange = {
                    value.value = it
                    onValueChange(it)
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
            )
        }

        Spacer(modifier = Modifier.size(5.dp))

        AnimatedVisibility(
            visible = value.value.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                enabled = value.value.isNotEmpty(),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .then(Modifier.size(24.dp)),
                onClick = {
                    value.value = ""
                    onValueChange("")
                }
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
