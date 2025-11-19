package com.example.pruebas_apis.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.cinemiron.ui.theme.*

private val DarkColorScheme = darkColorScheme(
    primary = AccentLight,
    secondary = Accent,
    tertiary = PrimaryLight,
    background = PrimaryDark,
    surface = Primary,
    onPrimary = PrimaryDark,
    onSecondary = PrimaryDark,
    onTertiary = PrimaryDark,
    onBackground = AccentLight,
    onSurface = AccentLight,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = PrimaryDark,
    tertiary = PrimaryLight,
    background = AccentLight,
    surface = Accent,
    onPrimary = AccentLight,
    onSecondary = AccentLight,
    onTertiary = PrimaryDark,
    onBackground = PrimaryDark,
    onSurface = PrimaryDark
)

@Composable
fun CineMironTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}