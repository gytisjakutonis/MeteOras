package gj.meteoras.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun UiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) darkColors else lightColors,
        content = content,
    )
}

private val lightColors = Colors(
    primary = Color(0xFF1b5e20),
    primaryVariant = Color(0xFF003300),
    secondary = Color(0xFF1565c0),
    secondaryVariant = Color(0xFF003c8f),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    true
)

private val darkColors = Colors(
    primary = Color(0xFF66bb6a),
    primaryVariant = Color(0xFF003300),
    secondary = Color(0xFF2196f3),
    secondaryVariant = Color(0xFF003c8f),
    background = Color.Black,
    surface = Color.Black,
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    false
)

val Colors.supplementary: Color
    get() = if (isLight) Color.LightGray else Color.DarkGray
