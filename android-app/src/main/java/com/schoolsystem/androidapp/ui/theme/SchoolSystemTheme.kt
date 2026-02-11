package com.schoolsystem.androidapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3D5AFE),
    onPrimary = Color.White,
    secondary = Color(0xFF00BFA5),
    onSecondary = Color.Black,
    tertiary = Color(0xFFFFB300),
    background = Color(0xFFF7F8FD),
    onBackground = Color(0xFF121212),
    surface = Color.White,
    onSurface = Color(0xFF1F1F1F)
)

private val AppTypography = Typography()

@Composable
fun SchoolSystemTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}
