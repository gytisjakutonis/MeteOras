package gj.meteoras.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

val Typography.body0: TextStyle
    @Composable
    get() = MaterialTheme.typography.body1.copy(fontSize = 18.sp)
