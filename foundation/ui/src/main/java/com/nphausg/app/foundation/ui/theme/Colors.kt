package com.nphausg.app.foundation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color.White,

    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color.Black,

    tertiary = Color(0xFFB00020),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF370617),
    onTertiaryContainer = Color.White,

    background = Color(0xFFFFFFFF),
    onBackground = Color.Black,

    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color.Black
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF6200EE),
    onPrimaryContainer = Color.White,

    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF03DAC5),
    onSecondaryContainer = Color.Black,

    tertiary = Color(0xFFCF6679),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFB00020),
    onTertiaryContainer = Color.White,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF121212),
    onSurface = Color.White,

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White
)

object Colors {
    val primary: Color
        @Composable
        get() = MaterialTheme.colorScheme.primary
}