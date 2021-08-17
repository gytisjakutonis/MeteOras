package gj.meteoras.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

private val primaryWhite = Color(0xFFEFF5EF)
private val secondaryWhite = Color(0xFFF4F7FA)
private val primaryBlack = Color(0xFF003300)
private val secondaryBlack = Color(0xFF003c8f)

internal val lightColors = Colors(
    primary = Color(0xFF1b5e20),
    primaryVariant = primaryBlack,
    secondary = Color(0xFF1565c0),
    secondaryVariant = secondaryBlack,
    background = primaryWhite,
    surface = primaryWhite,
    error = Color(0xFFB00020),
    onPrimary = primaryWhite,
    onSecondary = secondaryWhite,
    onBackground = primaryBlack,
    onSurface = primaryBlack,
    onError = primaryWhite,
    true
)

internal val darkColors = Colors(
    primary = Color(0xFF66bb6a),
    primaryVariant = primaryBlack,
    secondary = Color(0xFF2196f3),
    secondaryVariant = secondaryBlack,
    background = primaryBlack,
    surface = primaryBlack,
    error = Color(0xFFCF6679),
    onPrimary = primaryBlack,
    onSecondary = secondaryBlack,
    onBackground = primaryWhite,
    onSurface = primaryWhite,
    onError = primaryBlack,
    false
)

val Colors.supplementary: Color
    get() = if (isLight) Color.LightGray else Color.LightGray

val Colors.cold: Color
    get() = if (isLight) Color(0xFF1565c0) else Color(0xFF2196f3)

val Colors.hot: Color
    get() = if (isLight) Color(0xFFB00020) else Color(0xFFCF6679)
