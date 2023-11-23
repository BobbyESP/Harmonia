package com.kyant.ui.style.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kyant.m3color.hct.Hct
import com.kyant.ui.util.lerp
import com.kyant.ui.util.transformInHct

val LocalEmphasis = compositionLocalOf { Emphasis.Default }

@Immutable
data class Emphasis(
    val contentEmphasis: Float = 1f,
    val iconEmphasis: Float = 1f
) {
    companion object {
        @Stable
        val Default = Emphasis()
    }
}

@Composable
internal fun Color.applyEmphasis(emphasis: Float): Color {
    return when (emphasis) {
        1f -> this
        0f -> LocalColorSet.current.color.copy(alpha = alpha)
        else -> {
            val colorSet = LocalColorSet.current
            this.transformInHct {
                tone = lerp(
                    Hct.fromInt(colorSet.color.toArgb()).tone,
                    Hct.fromInt(colorSet.onColor.toArgb()).tone,
                    emphasis
                )
            }
        }
    }
}

@Composable
fun ProvideEmphasis(
    contentEmphasis: Float = LocalEmphasis.current.contentEmphasis,
    iconEmphasis: Float = LocalEmphasis.current.iconEmphasis,
    content: @Composable () -> Unit
) {
    val mergedEmphasis = Emphasis(contentEmphasis, iconEmphasis)
    CompositionLocalProvider(LocalEmphasis provides mergedEmphasis, content = content)
}
