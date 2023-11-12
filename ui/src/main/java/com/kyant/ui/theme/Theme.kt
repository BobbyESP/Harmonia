package com.kyant.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.kyant.ui.ProvideTextStyle
import com.kyant.ui.ripple.LocalRippleTheme
import com.kyant.ui.ripple.RippleTheme
import com.kyant.ui.ripple.rememberRipple

@Composable
fun Theme(
    colorScheme: ColorScheme = Theme.colorScheme,
    typography: Typography = Theme.typography,
    content: @Composable () -> Unit
) {
    val rippleIndication = rememberRipple(color = LocalColorToken.current.contentColor)
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
        LocalRippleTheme provides ThemedRippleTheme,
        LocalTextSelectionColors provides selectionColors,
        LocalTypography provides typography,
        LocalOverscrollConfiguration provides null
    ) {
        ProvideTextStyle(value = typography.bodyLarge, content = content)
    }
}

object Theme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@Immutable
private object ThemedRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = LocalColorToken.current.contentColor,
        lightTheme = true
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalColorToken.current.contentColor,
        lightTheme = !LocalColorScheme.current.darkTheme
    )
}
