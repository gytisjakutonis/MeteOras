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
    primary = Color(0xFF1b5e20),
    primaryVariant = primaryBlack,
    secondary = Color(0xFF1565c0),
    secondaryVariant = secondaryBlack,
    background = primaryWhite,
    surface = primaryWhite,
    error = Color(0xFFB00020),
    onPrimary = primaryWhite,
    onSecondary = primaryWhite,
    onBackground = primaryBlack,
    onSurface = primaryBlack,
    onError = primaryWhite,
    false
)

val Colors.seed: Color
    get() = Color(0xFFCE93D8)

