package com.kyant.ui.style.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

internal val BaselineColor = Color(0xFF6750A4)

@Stable
val BaselineLightColorScheme = ColorScheme(ColorTheme(BaselineColor, false))

@Stable
val BaselineDarkColorScheme = ColorScheme(ColorTheme(BaselineColor, true))
