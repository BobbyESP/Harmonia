package com.kyant.music.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.kyant.ui.theme.Theme
import com.kyant.ui.theme.color.ColorScheme
import com.kyant.ui.theme.color.ColorTheme
import com.kyant.ui.theme.colorScheme

@Composable
fun DefaultTheme(content: @Composable () -> Unit) {
    val sourceColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        colorResource(id = android.R.color.system_accent1_600)
    } else {
        colorScheme.theme.sourceColor
    }
    val isDark = isSystemInDarkTheme()
    val colorScheme = ColorScheme(ColorTheme(sourceColor, isDark))

    Theme(colorScheme = colorScheme, content = content)
}
