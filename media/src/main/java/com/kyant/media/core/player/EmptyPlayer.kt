package com.kyant.media.core.player

import android.os.Bundle
import com.kyant.media.core.item.MediaItem
import com.kyant.media.core.item.PlayQueue

/**
 * An empty [Player] that does nothing, but implements the [listeners].
 */
open class EmptyPlayer : Player {

    override val listeners: MutableList<Player.Listener> = mutableListOf()

    override var playbackState: Player.PlaybackState = Player.PlaybackState.Idle
        set(value) {
            field = value
            listeners.forEach { it.onPlaybackStateChanged(value) }
        }

    override var isPlaying: Boolean = false
        set(value) {
            field = value
            listeners.forEach { it.onIsPlayingChanged(value) }
        }

    override var position: Long = 0
        set(value) {
            field = value
            listeners.forEach { it.onPositionChanged(value) }
        }

    override var playbackSpeed: Float = 1f
        set(value) {
            field = value
            listeners.forEach { it.onPlaybackSpeedChanged(value) }
        }

    override var playQueue: PlayQueue = PlayQueue()
        set(value) {
            field = value
            listeners.forEach { it.onPlayQueueChanged(value) }
        }

    override var currentIndex: Int = -1
        set(value) {
            field = value
            listeners.forEach { it.onCurrentIndexChanged(value) }
        }

    override var currentMediaItem: MediaItem? = null
        set(value) {
            field = value
            listeners.forEach { it.onCurrentMediaItemChanged(value) }
        }

    override var repeatMode: RepeatMode = RepeatMode.Off
        set(value) {
            field = value
            listeners.forEach { it.onRepeatModeChanged(value) }
        }

    override var shuffleMode: ShuffleMode = ShuffleMode.Off
        set(value) {
            field = value
            listeners.forEach { it.onShuffleModeChanged(value) }
        }

    override fun play() {}

    override fun pause() {}

    override fun stop() {}

    override fun seekTo(position: Long) {}

    override fun release() {
        listeners.clear()
    }

    override fun playFromMediaId(mediaId: String?, extras: Bundle?) {}

    override fun skipToPrevious() {}

    override fun skipToNext() {}

    override fun changeRepeatMode(repeatMode: RepeatMode) {
        this.repeatMode = repeatMode
    }

    override fun changeShuffleMode(shuffleMode: ShuffleMode) {
        this.shuffleMode = shuffleMode
    }
}
