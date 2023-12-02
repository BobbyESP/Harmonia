package com.kyant.music.config

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KProperty

fun <T> mutableConfigStateOf(
    key: String,
    initialValue: T,
    converter: ConfigConverter<T> = ConfigConverter.default()
): MutableState<T> = MutableConfigState(key, initialValue, converter)

class MutableConfigState<T>(
    private val key: String,
    initialValue: T,
    private val converter: ConfigConverter<T>
) : MutableState<T> by mutableStateOf(initialValue) {

    override fun component1(): T {
        return configStore?.read(key)?.let { converter.load(it) } ?: value
    }

    override fun component2(): (T) -> Unit = {
        configStore?.edit(key, converter.save(it))
        value = it
    }
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> MutableState<T>.getValue(thisObj: Any?, property: KProperty<*>): T = component1()

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> MutableState<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    component2()(value)
}
