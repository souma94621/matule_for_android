package com.example.uikit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MatuleColorScheme = lightColorScheme(
    primary = MatulePrimary,
    onPrimary = Color.White,
    background = MatuleBackground,
    surface = MatuleSurfaceMuted,
    error = MatuleError,
    onError = Color.White
)

@Composable
fun MatuleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MatuleColorScheme,
        typography = MatuleTypography,
        content = content
    )
}