package com.example.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode(val displayName: String, val icon: String, val description: String) {
    DARK("Modo Oscuro", "🌙", "Estética gamer con acentos cian y violeta neón"),
    LIGHT("Modo Claro", "☀️", "Superficies limpias y texto de alta nitidez para luz de día"),
    HIGH_CONTRAST("Alto Contraste", "👁️", "Negro absoluto y bordes ultra-nítidos para máxima visibilidad")
}

data class AppPalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primary: Color,
    val secondary: Color,
    val isDark: Boolean
)

val DarkPalette = AppPalette(
    background = Color(0xFF090D16),
    surface = Color(0xFF131A29),
    surfaceVariant = Color(0xFF1E273D),
    surfaceBorder = Color(0xFF2E3D5C),
    textPrimary = Color(0xFFF3F4F6),
    textSecondary = Color(0xFF9CA3AF),
    textMuted = Color(0xFF6B7280),
    primary = AccentTeal,
    secondary = AccentViolet,
    isDark = true
)

val LightPalette = AppPalette(
    background = Color(0xFFF1F5F9),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE2E8F0),
    surfaceBorder = Color(0xFFCBD5E1),
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF334155),
    textMuted = Color(0xFF64748B),
    primary = Color(0xFF0284C7),
    secondary = Color(0xFF7C3AED),
    isDark = false
)

val HighContrastPalette = AppPalette(
    background = Color(0xFF000000),
    surface = Color(0xFF0A0A0A),
    surfaceVariant = Color(0xFF1A1A1A),
    surfaceBorder = Color(0xFF00FFFF),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFFFFF00),
    textMuted = Color(0xFF00FF88),
    primary = Color(0xFF00FFFF),
    secondary = Color(0xFFFF007F),
    isDark = true
)

val LocalAppPalette = staticCompositionLocalOf { DarkPalette }

object AppTheme {
    val colors: AppPalette
        @Composable
        get() = LocalAppPalette.current
}

private val DarkColorScheme = darkColorScheme(
    primary = AccentTeal,
    onPrimary = Color(0xFF00363A),
    primaryContainer = Color(0xFF004F54),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = AccentViolet,
    onSecondary = Color(0xFF280068),
    secondaryContainer = Color(0xFF3F1980),
    onSecondaryContainer = Color(0xFFE9DDFF),
    tertiary = RunGreatGreen,
    background = Color(0xFF090D16),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF131A29),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1E273D),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF2E3D5C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    background = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF334155),
    outline = Color(0xFFCBD5E1)
)

private val HighContrastColorScheme = darkColorScheme(
    primary = Color(0xFF00FFFF),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF003333),
    onPrimaryContainer = Color(0xFF00FFFF),
    secondary = Color(0xFFFF007F),
    onSecondary = Color.White,
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF0A0A0A),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFFFFFF00),
    outline = Color(0xFF00FFFF)
)

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit,
) {
    val palette = when (themeMode) {
        ThemeMode.DARK -> DarkPalette
        ThemeMode.LIGHT -> LightPalette
        ThemeMode.HIGH_CONTRAST -> HighContrastPalette
    }

    val colorScheme = when (themeMode) {
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.HIGH_CONTRAST -> HighContrastColorScheme
    }

    CompositionLocalProvider(LocalAppPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
