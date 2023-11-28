package com.kyant.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kyant.ui.style.Theme
import com.kyant.ui.style.color.ColorScheme
import com.kyant.ui.style.color.ColorTheme

@Composable
fun DefaultTheme(content: @Composable () -> Unit) {
    val sourceColor = ColorToken.sourceColor
    val isDark = !ColorToken.isLight
    val colorScheme = remember(sourceColor, isDark) {
        ColorScheme(ColorTheme(sourceColor, isDark))
    }

    Theme(colorScheme = colorScheme, content = content)
}
