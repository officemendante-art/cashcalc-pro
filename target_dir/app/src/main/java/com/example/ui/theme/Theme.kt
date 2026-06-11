package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CleanLightColorScheme = lightColorScheme(
    primary = DarkCharcoal,
    secondary = MutedGrey,
    background = BackgroundOffWhite,
    surface = CardSurfaceWhite,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    onPrimary = CardSurfaceWhite,
    onSecondary = DarkCharcoal,
    outline = BorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Force Light Mode
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve Braun/Apple aesthetic
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CleanLightColorScheme,
        typography = Typography,
        content = content
    )
}

