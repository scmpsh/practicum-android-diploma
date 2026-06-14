package ru.practicum.android.diploma.ui.theme

import androidx.compose.ui.graphics.Color

private const val HEX_BLACK = 0xFF1A1B22
private const val HEX_WHITE = 0xFFFDFDFD
private const val HEX_BLUE = 0xFF3772E7
private const val HEX_RED = 0xFFF56B6C
private const val HEX_GREY = 0xFFAEAFB4
private const val HEX_LIGHT_GREY = 0xFFE6E8EB
private const val HEX_SURFACE_CONTAINER_LIGHT = 0xFFF1F3F5
private const val HEX_SURFACE_VARIANT_DARK = 0xFF2A2C35
private const val OVERLAY_BACKGROUND_HEX = 0x801A1B22  // Полный hex с альфой

// Базовые цвета
val Black = Color(HEX_BLACK)
val White = Color(HEX_WHITE)
val Blue = Color(HEX_BLUE)
val Red = Color(HEX_RED)
val Grey = Color(HEX_GREY)
val LightGrey = Color(HEX_LIGHT_GREY)


val OverlayBackground = Color(OVERLAY_BACKGROUND_HEX)

// Light Theme
val LightPrimary = Blue
val LightOnPrimary = White
val LightSecondary = Grey
val LightOnSecondary = Black
val LightBackground = White
val LightOnBackground = Black
val LightSurface = White
val LightOnSurface = Black
val LightSurfaceVariant = LightGrey
val LightOnSurfaceVariant = Grey
val LightSurfaceContainer = Color(HEX_SURFACE_CONTAINER_LIGHT)
val LightInversionOnSurface = Black
val LightError = Red

// Dark Theme
val DarkPrimary = Blue
val DarkOnPrimary = White
val DarkSecondary = Grey
val DarkOnSecondary = White
val DarkBackground = Black
val DarkOnBackground = White
val DarkSurface = Black
val DarkOnSurface = White
val DarkSurfaceVariant = Color(HEX_SURFACE_VARIANT_DARK)
val DarkOnSurfaceVariant = Grey
val DarkSurfaceContainer = Color(HEX_SURFACE_VARIANT_DARK)
val DarkInversionOnSurface = White
val DarkError = Red
