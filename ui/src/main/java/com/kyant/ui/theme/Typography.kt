package com.kyant.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
    val displayLarge: TextStyle = TypographyTokens.DisplayLarge,
    val displayMedium: TextStyle = TypographyTokens.DisplayMedium,
    val displaySmall: TextStyle = TypographyTokens.DisplaySmall,
    val headlineLarge: TextStyle = TypographyTokens.HeadlineLarge,
    val headlineMedium: TextStyle = TypographyTokens.HeadlineMedium,
    val headlineSmall: TextStyle = TypographyTokens.HeadlineSmall,
    val titleLarge: TextStyle = TypographyTokens.TitleLarge,
    val titleMedium: TextStyle = TypographyTokens.TitleMedium,
    val titleSmall: TextStyle = TypographyTokens.TitleSmall,
    val bodyLarge: TextStyle = TypographyTokens.BodyLarge,
    val bodyMedium: TextStyle = TypographyTokens.BodyMedium,
    val bodySmall: TextStyle = TypographyTokens.BodySmall,
    val labelLarge: TextStyle = TypographyTokens.LabelLarge,
    val labelMedium: TextStyle = TypographyTokens.LabelMedium,
    val labelSmall: TextStyle = TypographyTokens.LabelSmall
)

internal val LocalTypography = staticCompositionLocalOf {
    Typography()
}

internal object TypographyTokens {
    val BodyLarge = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.5.sp
    )
    val BodyMedium = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.2.sp
    )
    val BodySmall = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.4.sp
    )
    val DisplayLarge = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.0.sp,
        letterSpacing = (-0.2).sp
    )
    val DisplayMedium = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.0.sp,
        letterSpacing = 0.0.sp
    )
    val DisplaySmall = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.0.sp,
        letterSpacing = 0.0.sp
    )
    val HeadlineLarge = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.0.sp,
        letterSpacing = 0.0.sp
    )
    val HeadlineMedium = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.0.sp
    )
    val HeadlineSmall = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.0.sp,
        letterSpacing = 0.0.sp
    )
    val LabelLarge = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    )
    val LabelMedium = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    )
    val LabelSmall = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp
    )
    val TitleLarge = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Brand,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.0.sp,
        letterSpacing = 0.0.sp
    )
    val TitleMedium = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.2.sp
    )
    val TitleSmall = DefaultTextStyle.copy(
        fontFamily = TypefaceTokens.Plain,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp
    )
}

internal val DefaultTextStyle = TextStyle.Default.copy(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

internal object TypefaceTokens {
    val Brand = FontFamily.SansSerif
    val Plain = FontFamily.SansSerif
}
