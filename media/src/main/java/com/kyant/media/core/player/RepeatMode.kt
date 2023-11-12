package com.kyant.media.core.player

import android.support.v4.media.session.PlaybackStateCompat

/**
 * Repeat mode for the [player][Player].
 */
enum class RepeatMode {

    /** No repeat. */
    Off,

    /** Repeat one. */
    One,

    /** Repeat all. */
    All;

    internal fun toSessionRepeatMode(): Int {
        return when (this) {
            Off -> PlaybackStateCompat.REPEAT_MODE_NONE
            One -> PlaybackStateCompat.REPEAT_MODE_ONE
            All -> PlaybackStateCompat.REPEAT_MODE_ALL
        }
    }

    companion object {
        internal fun fromSessionRepeatMode(repeatMode: Int): RepeatMode {
            return when (repeatMode) {
                PlaybackStateCompat.REPEAT_MODE_NONE -> Off
                PlaybackStateCompat.REPEAT_MODE_ONE -> One
                PlaybackStateCompat.REPEAT_MODE_ALL -> All
                PlaybackStateCompat.REPEAT_MODE_GROUP -> All
                else -> Off
            }
        }
    }
}
