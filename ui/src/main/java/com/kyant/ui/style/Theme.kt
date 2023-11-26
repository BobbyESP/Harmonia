package com.kyant.ui.style

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.kyant.m3color.scheme.Variant
import com.kyant.ui.ProvideTextStyle
import com.kyant.ui.ripple
import com.kyant.ui.style.color.ColorScheme
import com.kyant.ui.style.color.LocalColorScheme
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.typo.LocalTypography
import com.kyant.ui.style.typo.Typography

val colorScheme: ColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalColorScheme.current

val typography: Typography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current

val Color.colorScheme
    @Suppress("RemoveRedundantQualifierName")
    @Composable
    @ReadOnlyComposable
    get() = ColorScheme(
        com.kyant.ui.style.colorScheme.theme.copy(sourceColor = this)
    )

val Color.monochromeColorScheme
    @Composable
    @ReadOnlyComposable
    get() = ColorScheme(
        colorScheme.theme.copy(sourceColor = this, variant = Variant.MONOCHROME)
    )

val Color.fidelityColorScheme
    @Composable
    @ReadOnlyComposable
    get() = ColorScheme(
        colorScheme.theme.copy(sourceColor = this, variant = Variant.FIDELITY)
    )

val Color.vibrantColorScheme
    @Composable
    @ReadOnlyComposable
    get() = ColorScheme(
        colorScheme.theme.copy(sourceColor = this, variant = Variant.VIBRANT)
    )

@Composable
fun Theme(
    colorScheme: ColorScheme = com.kyant.ui.style.colorScheme,
    typography: Typography = com.kyant.ui.style.typography,
    content: @Composable () -> Unit
) {
    val rippleIndication = ripple(color = LocalColorSet.current.onColor)
    val primaryColor = colorScheme.primary
    val selectionColors = remember(primaryColor) {
        TextSelectionColors(
            handleColor = primaryColor.color,
            backgroundColor = primaryColor.color.copy(alpha = 0.4f)
        )
    }
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalIndication provides rippleIndication,
        LocalTextSelectionColors provides selectionColors,
        LocalTypography provides typography
    ) {
        ProvideTextStyle(value = typography.bodyLarge, content = content)
    }
}
