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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kyant.media.session.MediaBrowserService
import com.kyant.music.service.LocalPlayer
import com.kyant.music.service.PlaybackService
import com.kyant.music.service.StatefulPlayer
import com.kyant.music.ui.MainScreen
import com.kyant.music.ui.RequestPermissionDialog
import com.kyant.music.util.hazeBlur
import com.kyant.ui.RootBackground
import com.kyant.ui.theme.Theme
import com.kyant.ui.theme.systemColorScheme
import com.kyant.ui.util.thenIf

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
            Theme(systemColorScheme()) {
                val density = LocalDensity.current
                val isDialogVisible by remember { derivedStateOf { dialogRect != Rect.Zero } }
                val dimAlpha by animateFloatAsState(targetValue = if (isDialogVisible) 0.3f else 0f)

                RootBackground(
                    modifier = Modifier
                        .fillMaxSize()
                        .thenIf(isDialogVisible) {
                            hazeBlur(
                                RoundRect(dialogRect, with(density) { 32.dp.toPx() }, with(density) { 32.dp.toPx() }),
                                backgroundColor = if (Theme.colorScheme.darkTheme) Color.Black else Color.White
                            )
                        }
                        .drawWithContent {
                            drawContent()
                            drawRect(Color.Black, alpha = dimAlpha)
                        }
                ) {
                    CompositionLocalProvider(value = LocalPlayer provides player) {
                        MainScreen.Container()
                    }
                }

                RequestPermissionDialog()
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
