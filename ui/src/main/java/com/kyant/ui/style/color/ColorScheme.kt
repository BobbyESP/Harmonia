package com.kyant.ui.style.color

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kyant.m3color.dynamiccolor.DynamicColor
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.DynamicScheme
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant
import com.kyant.m3color.scheme.Variant

val LocalColorScheme = compositionLocalOf { BaselineLightColorScheme }

@Stable
class ColorScheme(val theme: ColorTheme) : Colors {
    companion object {
        private val colors = MaterialDynamicColors()

        private fun DynamicColor.toColor(scheme: DynamicScheme) = Color(this.getArgb(scheme))
    }

    private val hct = Hct.fromInt(theme.sourceColor.toArgb())

    private val scheme = when (theme.variant) {
        Variant.TONAL_SPOT -> SchemeTonalSpot(hct, theme.isDark, theme.contrast)
        Variant.MONOCHROME -> SchemeMonochrome(hct, theme.isDark, theme.contrast)
        Variant.NEUTRAL -> SchemeNeutral(hct, theme.isDark, theme.contrast)
        Variant.VIBRANT -> SchemeVibrant(hct, theme.isDark, theme.contrast)
        Variant.EXPRESSIVE -> SchemeExpressive(hct, theme.isDark, theme.contrast)
        Variant.FIDELITY -> SchemeFidelity(hct, theme.isDark, theme.contrast)
        Variant.CONTENT -> SchemeContent(hct, theme.isDark, theme.contrast)
        Variant.RAINBOW -> SchemeRainbow(hct, theme.isDark, theme.contrast)
        Variant.FRUIT_SALAD -> SchemeFruitSalad(hct, theme.isDark, theme.contrast)
    }

    override val primaryColorRoles: Colors.Primary
        get() = object : Colors.Primary {
            override val color: Color
                get() = colors.primary().toColor(scheme)
            override val onColor: Color
                get() = colors.onPrimary().toColor(scheme)
            override val container: Color
                get() = colors.primaryContainer().toColor(scheme)
            override val onContainer: Color
                get() = colors.onPrimaryContainer().toColor(scheme)
            override val fixed: Color
                get() = colors.primaryFixed().toColor(scheme)
            override val fixedDim: Color
                get() = colors.primaryFixedDim().toColor(scheme)
            override val onFixed: Color
                get() = colors.onPrimaryFixed().toColor(scheme)
            override val onFixedVariant: Color
                get() = colors.onPrimaryFixedVariant().toColor(scheme)
        }

    override val secondaryColorRoles: Colors.Secondary
        get() = object : Colors.Secondary {
            override val color: Color
                get() = colors.secondary().toColor(scheme)
            override val onColor: Color
                get() = colors.onSecondary().toColor(scheme)
            override val container: Color
                get() = colors.secondaryContainer().toColor(scheme)
            override val onContainer: Color
                get() = colors.onSecondaryContainer().toColor(scheme)
            override val fixed: Color
                get() = colors.secondaryFixed().toColor(scheme)
            override val fixedDim: Color
                get() = colors.secondaryFixedDim().toColor(scheme)
            override val onFixed: Color
                get() = colors.onSecondaryFixed().toColor(scheme)
            override val onFixedVariant: Color
                get() = colors.onSecondaryFixedVariant().toColor(scheme)
        }

    override val tertiaryColorRoles: Colors.Tertiary
        get() = object : Colors.Tertiary {
            override val color: Color
                get() = colors.tertiary().toColor(scheme)
            override val onColor: Color
                get() = colors.onTertiary().toColor(scheme)
            override val container: Color
                get() = colors.tertiaryContainer().toColor(scheme)
            override val onContainer: Color
                get() = colors.onTertiaryContainer().toColor(scheme)
            override val fixed: Color
                get() = colors.tertiaryFixed().toColor(scheme)
            override val fixedDim: Color
                get() = colors.tertiaryFixedDim().toColor(scheme)
            override val onFixed: Color
                get() = colors.onTertiaryFixed().toColor(scheme)
            override val onFixedVariant: Color
                get() = colors.onTertiaryFixedVariant().toColor(scheme)
        }

    override val errorColorRoles: Colors.Error
        get() = object : Colors.Error {
            override val color: Color
                get() = colors.error().toColor(scheme)
            override val onColor: Color
                get() = colors.onError().toColor(scheme)
            override val container: Color
                get() = colors.errorContainer().toColor(scheme)
            override val onContainer: Color
                get() = colors.onErrorContainer().toColor(scheme)
        }

    override val surfaceColorRoles: Colors.Surface
        get() = object : Colors.Surface {
            override val color: Color
                get() = colors.surface().toColor(scheme)
            override val onColor: Color
                get() = colors.onSurface().toColor(scheme)
            override val onColorVariant: Color
                get() = colors.onSurfaceVariant().toColor(scheme)
            override val containerLowest: Color
                get() = colors.surfaceContainerLowest().toColor(scheme)
            override val containerLow: Color
                get() = colors.surfaceContainerLow().toColor(scheme)
            override val container: Color
                get() = colors.surfaceContainer().toColor(scheme)
            override val containerHigh: Color
                get() = colors.surfaceContainerHigh().toColor(scheme)
            override val containerHighest: Color
                get() = colors.surfaceContainerHighest().toColor(scheme)
            override val dim: Color
                get() = colors.surfaceDim().toColor(scheme)
            override val bright: Color
                get() = colors.surfaceBright().toColor(scheme)
        }

    override val inverseColorRoles: Colors.Inverse
        get() = object : Colors.Inverse {
            override val surface: Color
                get() = colors.inverseSurface().toColor(scheme)
            override val onSurface: Color
                get() = colors.inverseOnSurface().toColor(scheme)
            override val primary: Color
                get() = colors.inversePrimary().toColor(scheme)
        }

    override val outlineColorRoles: Colors.Outline
        get() = object : Colors.Outline {
            override val color: Color
                get() = colors.outline().toColor(scheme)
            override val variant: Color
                get() = colors.outlineVariant().toColor(scheme)
        }
}
