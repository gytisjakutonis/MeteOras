package gj.meteoras.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gj.meteoras.ui.theme.paddings
import gj.meteoras.ui.theme.supplementary

@Composable
fun ItemDivider() = Divider(
    color = MaterialTheme.colors.supplementary,
    thickness = 1.dp,
    modifier = Modifier.padding(
        top = MaterialTheme.paddings.itemPadding,
        bottom = MaterialTheme.paddings.itemPadding
    )
)
