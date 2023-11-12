package com.kyant.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColorToken = compositionLocalOf { ColorToken.Unspecified }

@Immutable
open class ColorValueToken(override val color: Color) : ColorToken(color, Color.Unspecified) {
    override fun toString(): String {
        return "ColorValueToken(color=$color)"
    }
}

@Immutable
open class ColorToken(
    open val color: Color,
    val contentColor: Color
) {
    class Scrim(color: Color) : ColorValueToken(color)

    class Primary(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class PrimaryContainer(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class InversePrimary(color: Color) : ColorValueToken(color)
    class PrimaryFixed(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class PrimaryFixedDim(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Secondary(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SecondaryContainer(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SecondaryFixed(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SecondaryFixedDim(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Tertiary(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class TertiaryContainer(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class TertiaryFixed(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class TertiaryFixedDim(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Surface(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceVariant(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class InverseSurface(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceTint(color: Color) : ColorValueToken(color)
    class SurfaceDim(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceBright(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceContainerLowest(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceContainerLow(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceContainer(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceContainerHigh(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class SurfaceContainerHighest(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Background(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Outline(color: Color) : ColorValueToken(color)
    class OutlineVariant(color: Color) : ColorValueToken(color)

    class Error(color: Color, contentColor: Color) : ColorToken(color, contentColor)
    class ErrorContainer(color: Color, contentColor: Color) : ColorToken(color, contentColor)

    class Shadow(color: Color) : ColorValueToken(color)

    @Stable
    fun inverse(): ColorToken = ColorToken(color = contentColor, contentColor = color)

    fun copy(
        color: Color = this.color,
        contentColor: Color = this.contentColor
    ): ColorToken = ColorToken(color, contentColor)

    override fun toString(): String {
        return "ColorToken(color=$color, contentColor=$contentColor)"
    }

    companion object {
        @Stable
        val Unspecified = ColorToken(Color.Unspecified, Color.Unspecified)
    }
}
