package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.ui.AppScreen
import com.kyant.ui.FilledTonalButton
import com.kyant.ui.Icon
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun LibraryNavigator.Home(navigator: Navigator<AppScreen>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SingleLineText(
                text = "Home",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .graphicsLayer {
                        translationX = (paneExpandProgressValue - targetPaneExpandProgress) * width / 2f
                    },
                color = colorScheme.primary.color,
                emphasis = lerp(
                    0.8f,
                    0.2f,
                    (paneExpandProgressValue - targetPaneExpandProgress).absoluteValue * 2f
                ),
                textAlign = TextAlign.Center,
                style = typography.headlineLarge
            )
        }
        if (MediaStore.songs.isEmpty()) {
            Surface(
                shape = Rounding.Large.asSmoothRoundedShape()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Scan your library",
                        modifier = Modifier.padding(16.dp, 8.dp),
                        style = typography.headlineSmall
                    )
                    val context = LocalContext.current
                    FilledTonalButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                MediaStore.scan(context)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        enabled = !MediaStore.isScanning
                    ) {
                        Text(text = "Scan")
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .clip(Rounding.Large.asSmoothRoundedShape()),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Surface(
                onClick = { expandPane() },
                shape = Rounding.ExtraSmall.asRoundedShape(),
                colorSet = colorScheme.surfaceContainerLowest
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {}
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Songs",
                            style = typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.5f
                    )
                }
            }
            Surface(
                onClick = { navigator.push(AppScreen.Settings) },
                shape = Rounding.ExtraSmall.asRoundedShape(),
                colorSet = colorScheme.surfaceContainerLowest
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {}
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Settings",
                            style = typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.5f
                    )
                }
            }
        }
    }
}
