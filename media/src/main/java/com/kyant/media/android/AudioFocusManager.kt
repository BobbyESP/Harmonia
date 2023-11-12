package com.kyant.media.android

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper

class AudioFocusManager(
    private val enabled: Boolean,
    private val audioManager: AudioManager,
    private val audioAttributes: AudioAttributes,
    private val isPlaying: Boolean,
    private val onGainAudioFocus: () -> Unit,
    private val onLoseAudioFocus: () -> Unit
) {
    private val focusLock = Any()
    private var playbackDelayed = false
    private var resumeOnFocusGain = true
    private val changeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN ->
                if (playbackDelayed || resumeOnFocusGain) {
                    synchronized(focusLock) {
                        playbackDelayed = false
                        resumeOnFocusGain = false
                    }
                    onGainAudioFocus()
                }

            AudioManager.AUDIOFOCUS_LOSS -> {
                // this is not a transient loss, shouldn't automatically resume for now
                synchronized(focusLock) {
                    resumeOnFocusGain = false
                    playbackDelayed = false
                }
                onLoseAudioFocus()
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                synchronized(focusLock) {
                    // only resume if playback is being interrupted
                    resumeOnFocusGain = isPlaying
                    playbackDelayed = false
                }
                onLoseAudioFocus()
            }
        }
    }

    private val request = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
            setAudioAttributes(audioAttributes)
            setAcceptsDelayedFocusGain(true)
            setWillPauseWhenDucked(true)
            setOnAudioFocusChangeListener(changeListener, Handler(Looper.getMainLooper()))
            build()
        }
    } else {
        null
    }

    fun requestFocus(onGranted: () -> Unit = {}) {
        if (enabled) {
            val requestAudioFocus = if (request != null) {
                audioManager.requestAudioFocus(request)
            } else {
                @Suppress("DEPRECATION")
                audioManager.requestAudioFocus(
                    changeListener,
                    audioAttributes.contentType,
                    audioAttributes.usage
                )
            }
            synchronized(focusLock) {
                when (requestAudioFocus) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                        playbackDelayed = false
                    }

                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                        playbackDelayed = false
                        onGranted()
                    }

                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                        playbackDelayed = true
                    }
                }
            }
        } else {
            onGranted()
        }
    }

    fun abandonFocus(onGranted: () -> Unit = {}) {
        if (enabled) {
            val abandonAudioFocus = if (request != null) {
                audioManager.abandonAudioFocusRequest(request)
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(changeListener)
            }
            synchronized(focusLock) {
                when (abandonAudioFocus) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                        playbackDelayed = false
                    }

                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                        playbackDelayed = false
                        resumeOnFocusGain = false
                        onGranted()
                    }

                    AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> {
                        playbackDelayed = true
                    }
                }
            }
        } else {
            onGranted()
        }
    }

    fun preventResumeOnFocusGain() {
        synchronized(focusLock) {
            resumeOnFocusGain = false
        }
    }
}
