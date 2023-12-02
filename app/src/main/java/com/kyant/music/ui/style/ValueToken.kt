package com.kyant.music.ui.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp

var valueToken = ValueToken

@Stable
object ValueToken {
    val safeBottomPadding
        @Composable
        @ReadOnlyComposable
        get() = 8.dp
}
