package gj.meteoras.ui.compose

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun Fading(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = Row {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = content
    )
}
