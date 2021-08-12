package gj.meteoras.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val screenPadding: Dp = 10.dp,
    val itemPadding: Dp = 10.dp
)

val MaterialTheme.paddings get() = Paddings()
