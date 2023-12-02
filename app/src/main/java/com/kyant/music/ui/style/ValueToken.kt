package com.kyant.music.ui.style

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp
import com.kyant.music.config.ConfigConverter
import com.kyant.music.config.getValue
import com.kyant.music.config.mutableConfigStateOf
import com.kyant.music.config.setValue

var valueToken = ValueToken

@Stable
object ValueToken {

    var lightMode by mutableConfigStateOf("light-mode", "-1")

    var safeBottomPadding by mutableConfigStateOf(
        "safe-bottom-padding",
        8.dp,
        ConfigConverter.Dp
    )
}
