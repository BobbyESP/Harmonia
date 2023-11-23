package com.kyant.ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColorSet = compositionLocalOf { ColorSet.Unspecified }

@Immutable
data class ColorSet(
    val color: Color = Color.Unspecified,
    val onColor: Color = Color.Unspecified,
    val onColorVariant: Color = onColor
) {
    @Stable
    fun copyAlpha(
        colorAlpha: Float = color.alpha,
        onColorAlpha: Float = onColor.alpha,
        onColorVariantAlpha: Float = onColorVariant.alpha
    ) = ColorSet(
        color.copy(alpha = colorAlpha),
        onColor.copy(alpha = onColorAlpha),
        onColorVariant.copy(alpha = onColorVariantAlpha)
    )

    companion object {
        @Stable
        val Unspecified = ColorSet()
    }
}
