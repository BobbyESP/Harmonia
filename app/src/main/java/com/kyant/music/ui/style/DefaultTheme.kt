package com.kyant.music.ui.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kyant.ui.style.Theme
import com.kyant.ui.style.color.ColorScheme
import com.kyant.ui.style.color.ColorTheme

@Composable
fun DefaultTheme(content: @Composable () -> Unit) {
    val sourceColor = colorToken.sourceColor
    val isDark = !colorToken.isLight
    val colorScheme = remember(sourceColor, isDark) {
        ColorScheme(ColorTheme(sourceColor, isDark))
    }

    Theme(colorScheme = colorScheme, content = content)
}
