package com.kyant.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.kyant.ui.style.colorScheme

object ColorToken {

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
