package com.kyant.media.session

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.kyant.media.R
import com.kyant.media.android.AndroidMediaPlayer
import com.kyant.media.android.MediaNotificationManager
import com.kyant.media.core.item.PlayQueue
import com.kyant.media.core.player.EmptyPlayer
import com.kyant.media.core.player.Player
import com.kyant.media.core.player.Player.Companion.TOGGLE_REPEAT_MODE
import com.kyant.media.core.player.Player.Companion.TOGGLE_SHUFFLE_MODE
import com.kyant.media.core.player.Player.PlaybackAction.Companion.toSessionPlaybackStateActions
import com.kyant.media.core.player.RepeatMode
import com.kyant.media.core.player.ShuffleMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A player that uses [MediaSessionCompat] to play media.
 */
class MediaSessionPlayer(
    private val service: MediaBrowserServiceCompat,
    private val handler: Handler,
    private val sessionActivity: Class<out Activity>,
    private val callback: MediaSessionCallback
) : EmptyPlayer() {

    private val mediaSession: MediaSessionCompat

    private val player = AndroidMediaPlayer(
        context = service,
        onPrepared = {
            handler.post {
                updateState {
                    playbackState = Player.PlaybackState.Ready
                }
                play()
            }
        },
        onTransition = {
            if (repeatMode != RepeatMode.One) {
                skipToNext()
            } else {
                playFromMediaId(currentMediaItem?.mediaId)
            }
        },
        onGainAudioFocus = ::play,
        onLoseAudioFocus = ::pause
    )

    private val mediaNotificationManager: MediaNotificationManager

    override val isPlaybackActive: Boolean
        get() = when (mediaSession.controller.playbackState?.state) {
            PlaybackStateCompat.STATE_FAST_FORWARDING,
            PlaybackStateCompat.STATE_REWINDING,
            PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS,
            PlaybackStateCompat.STATE_SKIPPING_TO_NEXT,
            PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM,
            PlaybackStateCompat.STATE_BUFFERING,
            PlaybackStateCompat.STATE_CONNECTING,
            PlaybackStateCompat.STATE_PLAYING -> true

            else -> false
        } && mediaSession.controller.queue?.size != 0

    override val realPosition: Long
        get() = player.currentPosition

    override fun play() {
        mediaSession.controller.transportControls.play()
    }

    override fun pause() {
        mediaSession.controller.transportControls.pause()
    }

    override fun stop() {
        mediaSession.controller.transportControls.stop()
    }

    override fun seekTo(position: Long) {
        mediaSession.controller.transportControls.seekTo(position)
    }

    override fun release() {
        super.release()
        updateState {
            playbackState = Player.PlaybackState.Idle
        }
        player.release()
        mediaSession.release()
        stopService()
    }

    override fun playFromMediaId(mediaId: String?, extras: Bundle?) {
        mediaSession.controller.transportControls.playFromMediaId(mediaId, extras)
    }

    override fun skipToNext() {
        mediaSession.controller.transportControls.skipToNext()
    }

    override fun skipToPrevious() {
        mediaSession.controller.transportControls.skipToPrevious()
    }

    override fun changeRepeatMode(repeatMode: RepeatMode) {
        updateState {
            super.changeRepeatMode(repeatMode)
            position = player.currentPosition
        }
        mediaSession.setRepeatMode(repeatMode.toSessionRepeatMode())
    }

    override fun changeShuffleMode(shuffleMode: ShuffleMode) {
        updateState {
            super.changeShuffleMode(shuffleMode)
            position = player.currentPosition
        }
        mediaSession.setShuffleMode(shuffleMode.toSessionShuffleMode())
    }

    fun handleMediaButtonReceiverIntent(intent: Intent?) {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
    }

    private var isServiceStarted = false

    private fun startService() {
        if (isServiceStarted) return
        isServiceStarted = true
        ContextCompat.startForegroundService(
            service.applicationContext,
            Intent(service.applicationContext, service.javaClass)
        )
        val notification = mediaNotificationManager.buildMediaNotification(this) ?: return
        service.startForeground(1, notification)
    }

    private fun pauseService() {
        if (!isServiceStarted) return
        isServiceStarted = false
        service.stopForeground(Service.STOP_FOREGROUND_DETACH)
    }

    private fun stopService() {
        isServiceStarted = false
        service.stopForeground(Service.STOP_FOREGROUND_REMOVE)
        mediaNotificationManager.notificationManager.cancel(1)
    }

    private fun updateMediaNotification() {
        if (!isServiceStarted) return
        val notification = mediaNotificationManager.buildMediaNotification(this) ?: return
        mediaNotificationManager.notificationManager.notify(1, notification)
    }

    init {
        mediaSession = MediaSessionCompat(service, TAG).apply {
            service.sessionToken = sessionToken
            setSessionActivity(
                PendingIntent.getActivity(
                    service,
                    0,
                    Intent(service, sessionActivity),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            setCallback(
                object : MediaSessionCompat.Callback() {
                    private val scope = CoroutineScope(Dispatchers.Default)

                    private fun initPlayQueue() {
                        if (playQueue.isNotEmpty()) return
                        val mediaItems = callback.getRecentMediaItemsAtDeviceBootTime() ?: return
                        updateState {
                            playQueue = PlayQueue().apply { addAll(mediaItems) }
                        }
                        scope.launch {
                            setQueue(playQueue.asAndroidQueueItems())
                        }
                    }

                    private fun playQueueItem(queueId: Long) {
                        initPlayQueue()
                        val (index, mediaItem) = playQueue.getPlayableItemByQueueId(queueId) ?: return
                        updateState {
                            currentIndex = index
                            currentMediaItem = mediaItem
                            playbackState = Player.PlaybackState.Preparing
                        }
                        player.reset()
                        mediaItem.mediaDescription.mediaUri?.let { uri ->
                            player.setDataSource(uri)
                            setMetadata(mediaItem.asMediaMetadata())
                            player.prepareAsync()
                        }
                    }

                    private fun playQueueItem(mediaId: String) {
                        initPlayQueue()
                        val mediaItem = playQueue.playableItems.find { it.second.mediaId == mediaId }?.second ?: return
                        val index = playQueue.playableItems.indexOfFirst { it.second == mediaItem }
                        updateState {
                            currentIndex = index
                            currentMediaItem = mediaItem
                            playbackState = Player.PlaybackState.Preparing
                        }
                        player.reset()
                        mediaItem.mediaDescription.mediaUri?.let { uri ->
                            player.setDataSource(uri)
                            setMetadata(mediaItem.asMediaMetadata())
                            player.prepareAsync()
                        }
                    }

                    private fun playQueueItem(index: Int) {
                        initPlayQueue()
                        val mediaItem = playQueue.playableItems.getOrNull(index)?.second ?: return
                        updateState {
                            currentIndex = index
                            currentMediaItem = mediaItem
                            playbackState = Player.PlaybackState.Preparing
                        }
                        player.reset()
                        mediaItem.mediaDescription.mediaUri?.let { uri ->
                            player.setDataSource(uri)
                            setMetadata(mediaItem.asMediaMetadata())
                            player.prepareAsync()
                        }
                    }

                    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
                        if (mediaId == null) return
                        playQueueItem(mediaId)
                    }

                    override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
                        val mediaId = callback.onPlayFromUri(uri, extras)?.mediaId ?: return
                        playQueueItem(mediaId)
                    }

                    override fun onSkipToQueueItem(id: Long) {
                        playQueueItem(id)
                    }

                    override fun onPrepare() {
                        initPlayQueue()
                        playQueueItem(0)
                    }

                    override fun onPlay() {
                        updateState {
                            playbackState = Player.PlaybackState.Playing
                            isPlaying = true
                            position = player.currentPosition
                        }
                        startService()
                        player.play()
                    }

                    override fun onPause() {
                        updateState {
                            isPlaying = false
                            position = player.currentPosition
                        }
                        pauseService()
                        player.pause()
                    }

                    override fun onStop() {
                        updateState {
                            playbackState = Player.PlaybackState.Ended
                            isPlaying = false
                            position = player.currentPosition
                        }
                        stopService()
                        player.stop()
                    }

                    override fun onSeekTo(pos: Long) {
                        updateState {
                            position = pos
                        }
                        player.seekTo(pos)
                    }

                    override fun onRewind() {
                        seekTo(player.currentPosition - 10_000)
                    }

                    override fun onFastForward() {
                        seekTo(player.currentPosition + 10_000)
                    }

                    override fun onSkipToPrevious() {
                        playQueueItem(currentIndex - 1)
                    }

                    override fun onSkipToNext() {
                        playQueueItem(currentIndex + 1)
                    }

                    override fun onSetRepeatMode(repeatMode: Int) {
                        changeRepeatMode(RepeatMode.fromSessionRepeatMode(repeatMode))
                    }

                    override fun onSetShuffleMode(shuffleMode: Int) {
                        changeShuffleMode(ShuffleMode.fromSessionShuffleMode(shuffleMode))
                    }

                    override fun onCustomAction(action: String, extras: Bundle?) {
                        Log.i(TAG, "onCustomAction: $action")
                        when (action) {
                            TOGGLE_REPEAT_MODE -> toggleRepeatMode()
                            TOGGLE_SHUFFLE_MODE -> toggleShuffleMode()
                        }
                    }
                },
                handler
            )
        }.apply {
            mediaNotificationManager = MediaNotificationManager(service, sessionActivity, this)
            isActive = true
        }
    }

    private val lock = Any()

    private inline fun updateState(block: () -> Unit) {
        synchronized(lock) {
            val oldIndex = currentIndex
            block()
            val newIndex = currentIndex
            val position = if (oldIndex == newIndex) position else 0
            mediaSession.setPlaybackState(getPlaybackStateCompat(position))
            updateMediaNotification()
        }
    }

    private fun getPlaybackStateCompat(position: Long): PlaybackStateCompat {
        val actions = playbackState.getSupportedActions().toSessionPlaybackStateActions()
        val repeatModeCustomAction = when (repeatMode) {
            RepeatMode.Off -> repeatOffAction
            RepeatMode.One -> repeatOneAction
            RepeatMode.All -> repeatAllAction
        }
        val shuffleModeCustomAction = when (shuffleMode) {
            ShuffleMode.Off -> shuffleOffAction
            ShuffleMode.On -> shuffleOnAction
        }
        val playbackStateCompat = when (playbackState) {
            Player.PlaybackState.Idle -> PlaybackStateCompat.STATE_NONE
            Player.PlaybackState.Preparing -> PlaybackStateCompat.STATE_BUFFERING
            Player.PlaybackState.Ready -> PlaybackStateCompat.STATE_PAUSED
            Player.PlaybackState.Playing -> if (isPlaying) {
                PlaybackStateCompat.STATE_PLAYING
            } else {
                PlaybackStateCompat.STATE_PAUSED
            }

            Player.PlaybackState.Ended -> PlaybackStateCompat.STATE_STOPPED
        }
        return PlaybackStateCompat.Builder()
            .setActions(actions)
            .addCustomAction(repeatModeCustomAction)
            .addCustomAction(shuffleModeCustomAction)
            .setState(playbackStateCompat, position, playbackSpeed)
            .build()
    }

    companion object {

        private const val TAG = "MediaSessionPlayer"

        private val repeatOffAction = PlaybackStateCompat.CustomAction.Builder(
            TOGGLE_REPEAT_MODE,
            "Repeat Off",
            R.drawable.repeat_off
        ).build()
        private val repeatOneAction = PlaybackStateCompat.CustomAction.Builder(
            TOGGLE_REPEAT_MODE,
            "Repeat On",
            R.drawable.repeat_once
        ).build()
        private val repeatAllAction = PlaybackStateCompat.CustomAction.Builder(
            TOGGLE_REPEAT_MODE,
            "Repeat All",
            R.drawable.repeat
        ).build()

        private val shuffleOffAction = PlaybackStateCompat.CustomAction.Builder(
            TOGGLE_SHUFFLE_MODE,
            "Shuffle Off",
            R.drawable.shuffle_disabled
        ).build()
        private val shuffleOnAction = PlaybackStateCompat.CustomAction.Builder(
            TOGGLE_SHUFFLE_MODE,
            "Shuffle On",
            R.drawable.shuffle
        ).build()
    }
}
