package gj.meteoras.ui.compose

import androidx.activity.ComponentActivity
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun ComponentActivity.TopNavigationBar(
    title: String
) = TopAppBar(
    title = {
        Text(text = title)
    },
    navigationIcon = {
        IconButton(
            onClick = this::onBackPressed
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
)
