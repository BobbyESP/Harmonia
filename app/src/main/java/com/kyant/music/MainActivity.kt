package com.kyant.music

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import com.kyant.media.session.MediaBrowserService
import com.kyant.music.service.LocalPlayer
import com.kyant.music.service.PlaybackService
import com.kyant.music.service.StatefulPlayer
import com.kyant.music.ui.AppScreen
import com.kyant.music.ui.WelcomeDialog
import com.kyant.music.ui.theme.DefaultTheme
import com.kyant.ui.RootBackground

var dialogRect by mutableStateOf(Rect.Zero)

class MainActivity : ComponentActivity() {

    private val player = StatefulPlayer()

    private var isBound = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MediaBrowserService.LocalBinder
            player.setPlayer(binder.getPlayer())
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            DefaultTheme {
                RootBackground {
                    CompositionLocalProvider(value = LocalPlayer provides player) {
                        AppScreen.Container()
                    }
                }
                WelcomeDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, PlaybackService::class.java).apply {
            action = MediaBrowserService.SERVICE_PLAYER_INTERFACE
        }
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    public override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        if (!isBound) return
        unbindService(connection)
        isBound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isBound) return
        unbindService(connection)
        isBound = false
    }
}
