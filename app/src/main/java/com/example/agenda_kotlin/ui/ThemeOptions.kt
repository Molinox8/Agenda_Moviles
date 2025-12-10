package com.example.agenda_kotlin.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color

@Stable
enum class ThemeOption(val displayName: String) {
    AZUL("Azul") {
        override val colorScheme: ColorScheme = lightColorScheme(
            primary = Color(0xFF1565C0),
            onPrimary = Color.White,
            primaryContainer = Color(0xFF90CAF9),
            secondary = Color(0xFF00ACC1),
            secondaryContainer = Color(0xFFB2EBF2),
            background = Color(0xFFF5F5F5),
            surface = Color.White
        )
    },
    VERDE("Verde") {
        override val colorScheme: ColorScheme = lightColorScheme(
            primary = Color(0xFF2E7D32),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFA5D6A7),
            secondary = Color(0xFF558B2F),
            secondaryContainer = Color(0xFFC5E1A5),
            background = Color(0xFFF5F5F5),
            surface = Color.White
        )
    },
    MORADO("Morado") {
        override val colorScheme: ColorScheme = lightColorScheme(
            primary = Color(0xFF6A1B9A),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFCE93D8),
            secondary = Color(0xFF8E24AA),
            secondaryContainer = Color(0xFFE1BEE7),
            background = Color(0xFFF8F5FF),
            surface = Color.White
        )
    },
    NARANJA("Naranja") {
        override val colorScheme: ColorScheme = lightColorScheme(
            primary = Color(0xFFF57C00),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFFFCC80),
            secondary = Color(0xFFD84315),
            secondaryContainer = Color(0xFFFFAB91),
            background = Color(0xFFFFF8E1),
            surface = Color.White
        )
    },
    ROSADO("Rosado") {
        override val colorScheme: ColorScheme = lightColorScheme(
            primary = Color(0xFFE91E63),           // Pink 500
            onPrimary = Color.White,
            primaryContainer = Color(0xFFF8BBD0),  // Pink 200
            secondary = Color(0xFFC2185B),         // Pink 700
            secondaryContainer = Color(0xFFF48FB1),// Pink 300
            background = Color(0xFFFFF0F6),        // Soft rosy background
            surface = Color.White
        )
    };

    abstract val colorScheme: ColorScheme

    companion object {
        val saver: Saver<ThemeOption, String> = object : Saver<ThemeOption, String> {
            override fun restore(value: String): ThemeOption? =
                entries.firstOrNull { it.name == value }

            override fun SaverScope.save(value: ThemeOption): String = value.name
        }
    }
}

@Composable
fun AppTheme(
    themeOption: ThemeOption,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = themeOption.colorScheme,
        content = content
    )
}
