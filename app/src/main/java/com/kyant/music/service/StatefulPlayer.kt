package com.kyant.music.service

import android.os.Bundle
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.kyant.media.core.item.MediaItem
import com.kyant.media.core.item.PlayQueue
import com.kyant.media.core.player.EmptyPlayer
import com.kyant.media.core.player.Player
import com.kyant.media.core.player.RepeatMode
import com.kyant.media.core.player.ShuffleMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val LocalPlayer = staticCompositionLocalOf { StatefulPlayer() }

/**
 * A [Player] that can be used in Compose.
 */
@Stable
class StatefulPlayer : Player {

    private var player: Player = EmptyPlayer()

    override val listeners: MutableList<Player.Listener>
        get() = player.listeners

    override var playbackState: Player.PlaybackState by mutableStateOf(player.playbackState)

    override var isPlaying: Boolean by mutableStateOf(player.isPlaying)

    override var position: Long by mutableLongStateOf(player.position)

    override var playbackSpeed: Float by mutableFloatStateOf(player.playbackSpeed)

    override var playQueue: PlayQueue by mutableStateOf(player.playQueue)

    override var currentIndex: Int by mutableIntStateOf(player.currentIndex)

    override var currentMediaItem: MediaItem? by mutableStateOf(player.currentMediaItem)

    override var repeatMode: RepeatMode by mutableStateOf(player.repeatMode)

    override var shuffleMode: ShuffleMode by mutableStateOf(player.shuffleMode)

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Player.PlaybackState) {
            this@StatefulPlayer.playbackState = playbackState
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            this@StatefulPlayer.isPlaying = isPlaying
        }

        override fun onPositionChanged(position: Long) {
            this@StatefulPlayer.position = position
        }

        override fun onPlaybackSpeedChanged(playbackSpeed: Float) {
            this@StatefulPlayer.playbackSpeed = playbackSpeed
        }

        override fun onPlayQueueChanged(playQueue: PlayQueue) {
            this@StatefulPlayer.playQueue = playQueue
        }

        override fun onCurrentIndexChanged(currentIndex: Int) {
            this@StatefulPlayer.currentIndex = currentIndex
        }

        override fun onCurrentMediaItemChanged(currentMediaItem: MediaItem?) {
            this@StatefulPlayer.currentMediaItem = currentMediaItem
        }

        override fun onRepeatModeChanged(repeatMode: RepeatMode) {
            this@StatefulPlayer.repeatMode = repeatMode
        }

        override fun onShuffleModeChanged(shuffleMode: ShuffleMode) {
            this@StatefulPlayer.shuffleMode = shuffleMode
        }
    }

    fun setPlayer(player: Player) {
        this.player = player
        playbackState = player.playbackState
        isPlaying = player.isPlaying
        position = player.position
        playbackSpeed = player.playbackSpeed
        playQueue = player.playQueue
        currentIndex = player.currentIndex
        currentMediaItem = player.currentMediaItem
        repeatMode = player.repeatMode
        shuffleMode = player.shuffleMode
        player.listeners.add(listener)
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun release() {
        player.release()
    }

    override fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        player.playFromMediaId(mediaId, extras)
    }

    override fun skipToPrevious() {
        player.skipToPrevious()
    }

    override fun skipToNext() {
        player.skipToNext()
    }

    override fun changeRepeatMode(repeatMode: RepeatMode) {
        player.changeRepeatMode(repeatMode)
    }

    override fun changeShuffleMode(shuffleMode: ShuffleMode) {
        player.changeShuffleMode(shuffleMode)
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (isPlaying) {
                    position = player.realPosition
                }
                delay(1000)
            }
        }
    }
}
