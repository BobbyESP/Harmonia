package com.kyant.ui.theme.color

import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kyant.m3color.scheme.Variant

@Immutable
data class ColorTheme(
    val sourceColor: Color,
    val isDark: Boolean = false,
    @FloatRange(from = -1.0, to = 1.0)
    val contrast: Double = 0.0,
    val variant: Variant = Variant.TONAL_SPOT
)
