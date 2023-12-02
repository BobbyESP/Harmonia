package com.kyant.music.ui.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp
import com.kyant.music.config.ConfigConverter
import com.kyant.music.config.mutableConfigStateOf

var valueToken = ValueToken

@Stable
object ValueToken {

    val lightMode = mutableConfigStateOf("light-mode", "-1")

    val safeBottomPadding = mutableConfigStateOf(
        "safe-bottom-padding",
        8.dp,
        ConfigConverter.Dp
    )
}
