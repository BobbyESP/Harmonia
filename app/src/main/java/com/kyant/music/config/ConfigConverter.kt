package com.kyant.music.config

import androidx.compose.ui.unit.dp

class ConfigConverter<T>(
    val save: (T) -> String,
    val load: (String) -> T
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> default() = ConfigConverter({ it.toString() }, { it as T })

        val Dp = ConfigConverter({ it.toString() }, { it.dropLast(3).toFloat().dp })
    }
}
