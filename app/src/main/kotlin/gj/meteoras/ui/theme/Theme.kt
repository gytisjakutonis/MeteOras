package gj.meteoras.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val background = if (darkTheme) darkColors.background else lightColors.background

    SideEffect {
        systemUiController.setSystemBarsColor(color = background, darkIcons = !darkTheme)
        systemUiController.setNavigationBarColor(color = background, darkIcons = !darkTheme)
    }

    MaterialTheme(
        colors = if (darkTheme) darkColors else lightColors,
        content = content,
    )
}
