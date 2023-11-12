package com.kyant.music.data.song

import kotlinx.serialization.Serializable

@Serializable
data class AudioProperties(
    val duration: Long = 0,
    val bitrate: Int = 0,
    val sampleRate: Int = 0,
    val channels: Int = 0
) {
    companion object {
        val Empty = AudioProperties()
    }
}
