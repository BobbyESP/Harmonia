package com.kyant.music.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.colorResource
import com.kyant.ui.style.Theme
import com.kyant.ui.style.color.ColorScheme
import com.kyant.ui.style.color.ColorTheme
import com.kyant.ui.style.colorScheme

val isDark
    @Composable
    @ReadOnlyComposable
    get() = isSystemInDarkTheme()

@Composable
fun DefaultTheme(content: @Composable () -> Unit) {
    val sourceColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        colorResource(id = android.R.color.system_accent1_600)
    } else {
        colorScheme.theme.sourceColor
    }
    val colorScheme = ColorScheme(ColorTheme(sourceColor, isDark))

    Theme(colorScheme = colorScheme, content = content)
}
