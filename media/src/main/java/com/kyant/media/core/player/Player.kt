package com.kyant.media.core.player

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import com.kyant.media.core.item.MediaItem
import com.kyant.media.core.item.PlayQueue

/**
 * A player to play [MediaItem]s.
 */
interface Player {

    val listeners: MutableList<Listener>

    interface Listener {
        fun onPlaybackStateChanged(playbackState: PlaybackState)
        fun onIsPlayingChanged(isPlaying: Boolean)
        fun onPositionChanged(position: Long)
        fun onPlaybackSpeedChanged(playbackSpeed: Float)
        fun onPlayQueueChanged(playQueue: PlayQueue)
        fun onCurrentIndexChanged(currentIndex: Int)
        fun onCurrentMediaItemChanged(currentMediaItem: MediaItem?)
        fun onRepeatModeChanged(repeatMode: RepeatMode)
        fun onShuffleModeChanged(shuffleMode: ShuffleMode)
    }

    var playbackState: PlaybackState

    var isPlaying: Boolean

    var position: Long

    var playbackSpeed: Float

    var playQueue: PlayQueue

    var currentIndex: Int

    var currentMediaItem: MediaItem?

    val isPlaybackActive: Boolean
        get() = playbackState != PlaybackState.Idle &&
            playbackState != PlaybackState.Ended

    /**
     * The real-time position reported by the actual player.
     */
    val realPosition: Long
        get() = position

    var repeatMode: RepeatMode

    var shuffleMode: ShuffleMode

    fun play()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun release()

    fun playFromMediaId(mediaId: String?, extras: Bundle? = null)

    fun skipToPrevious()

    fun skipToNext()

    fun changeRepeatMode(repeatMode: RepeatMode)

    fun toggleRepeatMode() {
        val repeatMode = when (repeatMode) {
            RepeatMode.Off -> RepeatMode.One
            RepeatMode.One -> RepeatMode.All
            RepeatMode.All -> RepeatMode.Off
        }
        changeRepeatMode(repeatMode)
    }

    fun changeShuffleMode(shuffleMode: ShuffleMode)

    fun toggleShuffleMode() {
        val shuffleMode = when (shuffleMode) {
            ShuffleMode.Off -> ShuffleMode.On
            ShuffleMode.On -> ShuffleMode.Off
        }
        changeShuffleMode(shuffleMode)
    }

    enum class PlaybackState {
        Idle,
        Preparing,
        Ready,
        Playing,
        Ended;

        fun getSupportedActions(): List<PlaybackAction> {
            return when (this) {
                Idle -> listOf(
                    PlaybackAction.Prepare,
                    PlaybackAction.SkipToItem
                )

                Preparing -> listOf(
                    PlaybackAction.Stop,
                    PlaybackAction.SkipToItem,
                    PlaybackAction.SetPlaybackSpeed
                )

                Ready -> listOf(
                    PlaybackAction.Play,
                    PlaybackAction.Stop,
                    PlaybackAction.SeekTo,
                    PlaybackAction.SkipToItem,
                    PlaybackAction.SetPlaybackSpeed
                )

                Playing -> listOf(
                    PlaybackAction.Play,
                    PlaybackAction.Pause,
                    PlaybackAction.PlayPause,
                    PlaybackAction.Stop,
                    PlaybackAction.SeekTo,
                    PlaybackAction.SkipToItem,
                    PlaybackAction.SetPlaybackSpeed
                )

                Ended -> listOf(
                    PlaybackAction.Prepare,
                    PlaybackAction.Stop,
                    PlaybackAction.SkipToItem
                )
            }
        }
    }

    enum class PlaybackAction {
        Prepare,
        Play,
        Pause,
        PlayPause,
        Stop,
        SeekTo,
        SkipToItem,
        SetPlaybackSpeed;

        companion object {
            fun List<PlaybackAction>.toSessionPlaybackStateActions(): Long {
                var actions = 0L
                this.forEach { action ->
                    actions = actions or when (action) {
                        Prepare -> PlaybackStateCompat.ACTION_PREPARE
                        Play -> PlaybackStateCompat.ACTION_PLAY
                        Pause -> PlaybackStateCompat.ACTION_PAUSE
                        PlayPause -> PlaybackStateCompat.ACTION_PLAY_PAUSE
                        Stop -> PlaybackStateCompat.ACTION_STOP
                        SeekTo ->
                            PlaybackStateCompat.ACTION_SEEK_TO or
                                PlaybackStateCompat.ACTION_REWIND or
                                PlaybackStateCompat.ACTION_FAST_FORWARD

                        SkipToItem ->
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                                PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM

                        SetPlaybackSpeed -> PlaybackStateCompat.ACTION_SET_PLAYBACK_SPEED
                    }
                }
                actions = actions or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_URI or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                    PlaybackStateCompat.ACTION_SET_REPEAT_MODE or
                    PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
                return actions
            }
        }
    }

    companion object {
        internal const val TOGGLE_REPEAT_MODE = "com.kyant.media.core.player.TOGGLE_REPEAT_MODE"
        internal const val TOGGLE_SHUFFLE_MODE = "com.kyant.media.core.player.TOGGLE_SHUFFLE_MODE"
    }
}
