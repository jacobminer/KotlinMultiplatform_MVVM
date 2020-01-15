package com.jarroyo.kmp_mvvm

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme

/**
 * KMP_MVVM
 * Created by jake on 2020-01-15, 10:45 AM
 */
object Theme {
    private val lightThemeColors = ColorPalette(
        primary = Color(0xFFDD0D3C),
        primaryVariant = Color(0xFFC20029),
        onPrimary = Color.White,
        secondary = Color.White,
        onSecondary = Color.Black,
        surface = Color.White,
        background = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black,
        error = Color(0xFFD00036),
        onError = Color.White
    )

    private val darkThemeColors = ColorPalette(
        primary = Color(0xFF00ef7E),
        primaryVariant = Color(0xFF005E38),
        onPrimary = Color(0xFF242424),
        secondary = Color(0xFF121212),
        onSecondary = Color.White,
        surface = Color(0xFF242424),
        background = Color(0xFF242424),
        onBackground = Color.White,
        onSurface = Color.White
    )

    fun withTheme(children: @Composable() () -> Unit) {
        val darkTheme = +isSystemInDarkTheme()
        MaterialTheme(colors = if (darkTheme) Theme.darkThemeColors else Theme.lightThemeColors) {
            children()
        }
    }

    fun withDarkTheme(children: @Composable() () -> Unit) {
        MaterialTheme(darkThemeColors) {
            children()
        }
    }

    fun withLightTheme(children: @Composable() () -> Unit) {
        MaterialTheme(lightThemeColors) {
            children()
        }
    }
}