package gj.meteoras.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val screen: Dp = 10.dp,
    val medium: Dp = 10.dp,
    val small: Dp = 5.dp
)

val MaterialTheme.paddings get() = Paddings()
