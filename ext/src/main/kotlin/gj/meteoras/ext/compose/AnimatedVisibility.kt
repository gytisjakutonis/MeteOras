package gj.meteoras.ext.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@ExperimentalAnimationApi
@Composable
fun BoxScope.AnimatedVisibility(
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) = Row {
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        content = content
    )
}
