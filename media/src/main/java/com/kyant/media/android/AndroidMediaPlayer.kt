package com.kyant.media.android

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.kyant.media.Configs

class AndroidMediaPlayer(
    private val context: Context,
    private val onPrepared: () -> Unit = {},
    private val onTransition: () -> Unit = {},
    audioFocusEnabled: Boolean = true,
    onGainAudioFocus: () -> Unit = {},
    onLoseAudioFocus: () -> Unit = {}
) {
    private val audioAttributes = AudioAttributes.Builder().run {
        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        setUsage(AudioAttributes.USAGE_MEDIA)
        build()
    }

    private var state = State.Idle
        set(value) {
            log("setState", value)
            field = value
        }

    private val player = MediaPlayer().apply {
        setAudioAttributes(audioAttributes)
        setOnPreparedListener {
            logListener("onPrepared")
            onPrepared()
            state = State.Prepared
        }
        setOnCompletionListener {
            logListener("onCompletion")
            state = State.PlaybackCompleted
            onTransition()
        }
        setOnSeekCompleteListener {
            logListener("onSeekComplete")
        }
        setOnErrorListener { _, what, extra ->
            logListener("onError", what, extra)
            state = State.Error
            this@AndroidMediaPlayer.reset()
            true
        }
    }

    private val audioFocusManager = AudioFocusManager(
        enabled = audioFocusEnabled,
        audioManager = context.getSystemService(AudioManager::class.java),
        audioAttributes = audioAttributes,
        isPlaying = player.isPlaying,
        onGainAudioFocus = onGainAudioFocus,
        onLoseAudioFocus = onLoseAudioFocus
    )

    private val audioBecomingNoisyManager = AudioBecomingNoisyManager(
        context = context,
        onAudioBecomingNoisy = ::pause
    )

    val currentPosition: Long
        get() = synchronized(state) {
            if (state == State.Prepared ||
                state == State.Started ||
                state == State.Paused ||
                state == State.Stopped ||
                state == State.PlaybackCompleted ||
                state == State.End
            ) {
                player.currentPosition.toLong()
            } else {
                0L
            }
        }

    fun setDataSource(uri: Uri) {
        validateState("setDataSource", State.Idle) {
            log("setDataSource", uri)
            player.setDataSource(context, uri)
            state = State.Initialized
        }
    }

    fun prepareAsync() {
        validateState("prepareAsync", State.Initialized, State.Stopped) {
            log("prepareAsync")
            player.prepareAsync()
            state = State.Preparing
        }
    }

    fun play() {
        if (state == State.Stopped) {
            prepareAsync()
            return
        }
        if (state == State.Preparing) {
            return
        }
        validateState("start", State.Prepared, State.Started, State.Paused, State.PlaybackCompleted) {
            audioFocusManager.requestFocus {
                log("play")
                // start the player (custom call)
                player.start()
                state = State.Started
                // Register BECOME_NOISY BroadcastReceiver
                audioBecomingNoisyManager.setEnabled(true)
            }
        }
    }

    // Important:
    // Also set [resumeOnFocusGain] to false when the user pauses or stops playback: this way your
    // application doesn't automatically restart when it gains focus, even though the user had
    // stopped it.
    fun pause() {
        validateState("pause", State.Started, State.Paused, State.PlaybackCompleted) {
            log("pause")
            audioFocusManager.preventResumeOnFocusGain()
            // Update metadata and state
            // pause the player (custom call)
            player.pause()
            state = State.Paused
            // unregister BECOME_NOISY BroadcastReceiver
            audioBecomingNoisyManager.setEnabled(false)
        }
    }

    fun stop() {
        validateState("stop", State.Started, State.Stopped, State.Paused, State.PlaybackCompleted) {
            audioFocusManager.abandonFocus {
                log("stop")
                // stop the player (custom call)
                player.stop()
                state = State.Stopped
                // unregister BECOME_NOISY BroadcastReceiver
                audioBecomingNoisyManager.setEnabled(false)
            }
        }
    }

    fun seekTo(position: Long) {
        validateState("seekTo", State.Prepared, State.Started, State.Paused, State.PlaybackCompleted) {
            log("seekTo", position)
            player.seekTo(position.coerceAtLeast(0).toInt())
        }
    }

    fun reset() {
        synchronized(state) {
            log("reset")
            player.reset()
            state = State.Idle
        }
    }

    fun release() {
        synchronized(state) {
            log("release")
            player.release()
            state = State.End
            audioBecomingNoisyManager.setEnabled(false)
            audioFocusManager.preventResumeOnFocusGain()
            audioFocusManager.abandonFocus()
        }
    }

    private fun log(action: String, vararg messages: Any?) {
        if (Configs.ANDROID_MEDIA_PLAYER_DEBUG) {
            Log.d(TAG, "  $action(${messages.joinToString()})")
        }
    }

    private fun logListener(listener: String, vararg messages: Any?) {
        if (Configs.ANDROID_MEDIA_PLAYER_DEBUG) {
            Log.d(
                TAG,
                "triggered $listener(${messages.joinToString().replace("android.media.", "")})"
            )
        }
    }

    private inline fun validateState(
        action: String,
        vararg states: State,
        block: () -> Unit = {}
    ) {
        synchronized(state) {
            if (state !in states) {
                val message = "Cannot call $action in state $state"
                Log.e(TAG, message)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } else {
                if (Configs.ANDROID_MEDIA_PLAYER_DEBUG) {
                    Log.d(TAG, "Calling $action in state $state")
                }
                block()
            }
        }
    }

    companion object {
        private const val TAG = "AndroidMediaPlayer"

        private enum class State {
            Idle, Initialized, Preparing, Prepared, Started, Paused, Stopped, PlaybackCompleted, End, Error
        }
    }
}
