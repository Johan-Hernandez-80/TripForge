package com.example.tripforge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = LightGrayBackground,
    onPrimaryContainer = BluePrimary,
    
    secondary = TealSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6FFFB), // Derived from TealSecondary light version
    onSecondaryContainer = Color(0xFF0F766E),
    
    tertiary = OrangeAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFF7ED), // Derived from OrangeAccent light version
    onTertiaryContainer = Color(0xFF9A3412),
    
    background = BackgroundApp,
    onBackground = TextPrimary,
    
    surface = WhiteCard,
    onSurface = TextPrimary,
    surfaceVariant = LightGrayBackground,
    onSurfaceVariant = TextSecondary,
    
    outline = GrayBorder,
    outlineVariant = GrayBorder,
    
    error = ErrorRed,
    onError = Color.White,
    
    inverseSurface = TextPrimary,
    inverseOnSurface = BackgroundApp,
    inversePrimary = Color(0xFF60A5FA)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color(0xFF60A5FA),
    
    secondary = Color(0xFF2DD4BF),
    onSecondary = Color(0xFF0F172A),
    
    tertiary = Color(0xFFFB923C),
    onTertiary = Color(0xFF0F172A),
    
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    
    outline = Color(0xFF475569),
    error = Color(0xFFF87171)
)

@Composable
fun TripForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
