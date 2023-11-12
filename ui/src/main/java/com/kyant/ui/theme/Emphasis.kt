package com.kyant.ui.theme

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
        0f -> LocalColorToken.current.color.copy(alpha = alpha)
        else -> {
            val colorToken = LocalColorToken.current
            this.transformInHct {
                tone = lerp(
                    Hct.fromInt(colorToken.color.toArgb()).tone,
                    Hct.fromInt(colorToken.contentColor.toArgb()).tone,
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
