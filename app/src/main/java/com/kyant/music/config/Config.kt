package com.kyant.music.config

import androidx.compose.runtime.mutableStateMapOf

data class Config<T>(
    val key: String,
    val value: T,
    val initialValue: T
)

val Configs = mutableStateMapOf<String, Config<*>>()
