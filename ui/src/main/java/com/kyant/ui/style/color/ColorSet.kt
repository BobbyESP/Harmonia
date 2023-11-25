package com.kyant.ui.style.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
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

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun animateColorSet(
    targetValue: ColorSet,
    animationSpec: AnimationSpec<Color> = spring()
): ColorSet {
    return ColorSet(
        animateColorAsState(targetValue.color, animationSpec).value,
        animateColorAsState(targetValue.onColor, animationSpec).value,
        animateColorAsState(targetValue.onColorVariant, animationSpec).value
    )
}
