package gj.meteoras.ui.compose

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import gj.meteoras.ui.theme.supplementary

@Composable
fun ItemDivider() = Divider(
    color = MaterialTheme.colors.supplementary,
    thickness = 1.dp,
)
