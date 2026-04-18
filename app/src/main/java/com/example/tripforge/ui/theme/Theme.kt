package com.example.tripforge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = TealSecondary,
    tertiary = OrangeAccent,
    background = BackgroundApp,
    surface = WhiteCard,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = GrayBorder
)

@Composable
fun TripForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // For now, we only have LightColorScheme defined

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
