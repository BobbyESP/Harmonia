package com.kyant.music.config

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
fun <T> mutableConfigStateOf(
    key: String,
    initialValue: T,
    converter: ConfigConverter<T> = ConfigConverter.default()
) = MutableConfigState(key, initialValue, converter)

class MutableConfigState<T>(
    private val key: String,
    private val initialValue: T,
    private val converter: ConfigConverter<T>
) : MutableState<T> by mutableStateOf(
    configStore?.read(key)?.let { converter.load(it) } ?: initialValue
) {
    override var value: T = component1()
        set(value) {
            configStore?.edit(key, converter.save(value))
            field = value
        }

    init {
        Configs[key] = Config(key, component1(), initialValue)
    }

    fun reset() {
        value = initialValue
    }
}
