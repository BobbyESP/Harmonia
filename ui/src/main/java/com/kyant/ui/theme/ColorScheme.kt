package com.kyant.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import com.kyant.m3color.dynamiccolor.MaterialDynamicColors
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant

@Immutable
data class ColorScheme(
    val scrim: ColorToken.Scrim,

    val primary: ColorToken.Primary,
    val primaryContainer: ColorToken.PrimaryContainer,
    val inversePrimary: ColorToken.InversePrimary,
    val primaryFixed: ColorToken.PrimaryFixed,
    val primaryFixedDim: ColorToken.PrimaryFixedDim,

    val secondary: ColorToken.Secondary,
    val secondaryContainer: ColorToken.SecondaryContainer,
    val secondaryFixed: ColorToken.SecondaryFixed,
    val secondaryFixedDim: ColorToken.SecondaryFixedDim,

    val tertiary: ColorToken.Tertiary,
    val tertiaryContainer: ColorToken.TertiaryContainer,
    val tertiaryFixed: ColorToken.TertiaryFixed,
    val tertiaryFixedDim: ColorToken.TertiaryFixedDim,

    val surface: ColorToken.Surface,
    val surfaceVariant: ColorToken.SurfaceVariant,
    val surfaceTint: ColorToken.SurfaceTint,
    val inverseSurface: ColorToken.InverseSurface,
    val surfaceDim: ColorToken.SurfaceDim,
    val surfaceBright: ColorToken.SurfaceBright,
    val surfaceContainerLowest: ColorToken.SurfaceContainerLowest,
    val surfaceContainerLow: ColorToken.SurfaceContainerLow,
    val surfaceContainer: ColorToken.SurfaceContainer,
    val surfaceContainerHigh: ColorToken.SurfaceContainerHigh,
    val surfaceContainerHighest: ColorToken.SurfaceContainerHighest,

    val background: ColorToken.Background,

    val outline: ColorToken.Outline,
    val outlineVariant: ColorToken.OutlineVariant,

    val error: ColorToken.Error,
    val errorContainer: ColorToken.ErrorContainer,

    val shadow: ColorToken.Shadow,

    val darkTheme: Boolean
)

internal val LocalColorScheme = staticCompositionLocalOf { dynamicColorScheme() }

@Composable
fun systemColorScheme(
    isDark: Boolean = isSystemInDarkTheme(),
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = 0.0
): ColorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    dynamicColorScheme(
        colorResource(id = android.R.color.system_accent1_600),
        isDark,
        style,
        contrastLevel
    )
} else {
    dynamicColorScheme(isDark = isDark, style = style, contrastLevel = contrastLevel)
}

fun dynamicColorScheme(
    keyColor: Color = Color(0xFF00668B),
    isDark: Boolean = false,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = 0.0
): ColorScheme {
    val hct = Hct.fromInt(keyColor.toArgb())
    val colors = MaterialDynamicColors()
    val scheme = when (style) {
        PaletteStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrastLevel)
        PaletteStyle.Neutral -> SchemeNeutral(hct, isDark, contrastLevel)
        PaletteStyle.Vibrant -> SchemeVibrant(hct, isDark, contrastLevel)
        PaletteStyle.Expressive -> SchemeExpressive(hct, isDark, contrastLevel)
        PaletteStyle.Rainbow -> SchemeRainbow(hct, isDark, contrastLevel)
        PaletteStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrastLevel)
        PaletteStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrastLevel)
        PaletteStyle.Fidelity -> SchemeFidelity(hct, isDark, contrastLevel)
        PaletteStyle.Content -> SchemeContent(hct, isDark, contrastLevel)
    }
    val monochromeScheme = SchemeMonochrome(hct, isDark, contrastLevel)

    val onSurface = Color(colors.onSurface().getArgb(monochromeScheme))

    return ColorScheme(
        scrim = ColorToken.Scrim(
            Color(colors.scrim().getArgb(scheme))
        ),

        primary = ColorToken.Primary(
            Color(colors.primary().getArgb(scheme)),
            Color(colors.onPrimary().getArgb(scheme))
        ),
        primaryContainer = ColorToken.PrimaryContainer(
            Color(colors.primaryContainer().getArgb(scheme)),
            Color(colors.onPrimaryContainer().getArgb(scheme))
        ),
        inversePrimary = ColorToken.InversePrimary(
            Color(colors.inversePrimary().getArgb(scheme))
        ),
        primaryFixed = ColorToken.PrimaryFixed(
            Color(colors.primaryFixed().getArgb(scheme)),
            Color(colors.onPrimaryFixed().getArgb(scheme))
        ),
        primaryFixedDim = ColorToken.PrimaryFixedDim(
            Color(colors.primaryFixedDim().getArgb(scheme)),
            Color(colors.onPrimaryFixedVariant().getArgb(scheme))
        ),

        secondary = ColorToken.Secondary(
            Color(colors.secondary().getArgb(scheme)),
            Color(colors.onSecondary().getArgb(scheme))
        ),
        secondaryContainer = ColorToken.SecondaryContainer(
            Color(colors.secondaryContainer().getArgb(scheme)),
            Color(colors.onSecondaryContainer().getArgb(scheme))
        ),
        secondaryFixed = ColorToken.SecondaryFixed(
            Color(colors.secondaryFixed().getArgb(scheme)),
            Color(colors.onSecondaryFixed().getArgb(scheme))
        ),
        secondaryFixedDim = ColorToken.SecondaryFixedDim(
            Color(colors.secondaryFixedDim().getArgb(scheme)),
            Color(colors.onSecondaryFixedVariant().getArgb(scheme))
        ),

        tertiary = ColorToken.Tertiary(
            Color(colors.tertiary().getArgb(scheme)),
            Color(colors.onTertiary().getArgb(scheme))
        ),
        tertiaryContainer = ColorToken.TertiaryContainer(
            Color(colors.tertiaryContainer().getArgb(scheme)),
            Color(colors.onTertiaryContainer().getArgb(scheme))
        ),
        tertiaryFixed = ColorToken.TertiaryFixed(
            Color(colors.tertiaryFixed().getArgb(scheme)),
            Color(colors.onTertiaryFixed().getArgb(scheme))
        ),
        tertiaryFixedDim = ColorToken.TertiaryFixedDim(
            Color(colors.tertiaryFixedDim().getArgb(scheme)),
            Color(colors.onTertiaryFixedVariant().getArgb(scheme))
        ),

        surface = ColorToken.Surface(
            Color(colors.surface().getArgb(scheme)),
            onSurface
        ),
        surfaceVariant = ColorToken.SurfaceVariant(
            Color(colors.surfaceVariant().getArgb(scheme)),
            Color(colors.onSurfaceVariant().getArgb(scheme))
        ),
        surfaceTint = ColorToken.SurfaceTint(
            Color(colors.surfaceTint().getArgb(scheme))
        ),
        inverseSurface = ColorToken.InverseSurface(
            Color(colors.inverseSurface().getArgb(scheme)),
            Color(colors.inverseOnSurface().getArgb(scheme))
        ),
        surfaceDim = ColorToken.SurfaceDim(
            Color(colors.surfaceDim().getArgb(scheme)),
            onSurface
        ),
        surfaceBright = ColorToken.SurfaceBright(
            Color(colors.surfaceBright().getArgb(scheme)),
            onSurface
        ),
        surfaceContainerLowest = ColorToken.SurfaceContainerLowest(
            Color(colors.surfaceContainerLowest().getArgb(scheme)),
            onSurface
        ),
        surfaceContainerLow = ColorToken.SurfaceContainerLow(
            Color(colors.surfaceContainerLow().getArgb(scheme)),
            onSurface
        ),
        surfaceContainer = ColorToken.SurfaceContainer(
            Color(colors.surfaceContainer().getArgb(scheme)),
            onSurface
        ),
        surfaceContainerHigh = ColorToken.SurfaceContainerHigh(
            Color(colors.surfaceContainerHigh().getArgb(scheme)),
            onSurface
        ),
        surfaceContainerHighest = ColorToken.SurfaceContainerHighest(
            Color(colors.surfaceContainerHighest().getArgb(scheme)),
            onSurface
        ),

        background = ColorToken.Background(
            Color(colors.background().getArgb(scheme)),
            Color(colors.onBackground().getArgb(scheme))
        ),

        outline = ColorToken.Outline(
            Color(colors.outline().getArgb(scheme))
        ),
        outlineVariant = ColorToken.OutlineVariant(
            Color(colors.outlineVariant().getArgb(scheme))
        ),

        error = ColorToken.Error(
            Color(colors.error().getArgb(scheme)),
            Color(colors.onError().getArgb(scheme))
        ),
        errorContainer = ColorToken.ErrorContainer(
            Color(colors.errorContainer().getArgb(scheme)),
            Color(colors.onErrorContainer().getArgb(scheme))
        ),

        shadow = ColorToken.Shadow(
            Color(colors.shadow().getArgb(scheme))
        ),

        darkTheme = isDark
    )
}

enum class PaletteStyle {
    TonalSpot,
    Neutral,
    Vibrant,
    Expressive,
    Rainbow,
    FruitSalad,
    Monochrome,
    Fidelity,
    Content
}
