package com.kyant.media.core.player

import android.support.v4.media.session.PlaybackStateCompat

/**
 * Shuffle mode for the [player][Player].
 */
enum class ShuffleMode {

    /** No shuffle. */
    Off,

    /** Shuffle all. */
    On;

    internal fun toSessionShuffleMode(): Int {
        return when (this) {
            Off -> PlaybackStateCompat.SHUFFLE_MODE_NONE
            On -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        }
    }

    companion object {
        internal fun fromSessionShuffleMode(shuffleMode: Int): ShuffleMode {
            return when (shuffleMode) {
                PlaybackStateCompat.SHUFFLE_MODE_NONE -> Off
                PlaybackStateCompat.SHUFFLE_MODE_ALL -> On
                PlaybackStateCompat.SHUFFLE_MODE_GROUP -> On
                else -> Off
            }
        }
    }
}
