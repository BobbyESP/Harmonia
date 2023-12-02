package com.kyant.music.ui.style

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.colorResource
import com.kyant.ui.style.colorScheme

var colorToken = ColorToken

@Stable
object ColorToken {

    val sourceColor
        @Composable
        @ReadOnlyComposable
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            colorResource(id = android.R.color.system_accent1_600)
        } else {
            colorScheme.theme.sourceColor
        }

    val isLight
        @Composable
        @ReadOnlyComposable
        get() = !isSystemInDarkTheme()

    val background
        @Composable
        @ReadOnlyComposable
        get() = if (isLight) {
            colorScheme.surfaceContainerLow
        } else {
            colorScheme.surfaceContainerLowest
        }

    val card
        @Composable
        @ReadOnlyComposable
        get() = if (isLight) {
            colorScheme.surfaceContainerLowest
        } else {
            colorScheme.surfaceContainerLow
        }
}
