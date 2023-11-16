package com.kyant.music.ui.library

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.ui.MainScreen
import com.kyant.ui.Icon
import com.kyant.ui.LocalIconSize
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalScroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.theme.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Navigator<LibraryScreen>.Library(mainNavigator: Navigator<MainScreen>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .smoothVerticalScroll(rememberScrollState())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(16.dp)
                .sizeIn(minHeight = 48.dp)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompositionLocalProvider(value = LocalIconSize provides 18.dp) {
                Surface(
                    onClick = {},
                    modifier = Modifier.fillMaxHeight(),
                    shape = SmoothRoundedCornerShape(24.dp),
                    colorToken = Theme.colorScheme.primaryContainer,
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = false
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Apps)
                    }
                }
                Surface(
                    onClick = {},
                    modifier = Modifier.fillMaxHeight(),
                    shape = SmoothRoundedCornerShape(16.dp),
                    colorToken = Theme.colorScheme.surfaceContainerHigh,
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = false
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.QueueMusic)
                        Text(text = "Playlists")
                    }
                }
                Surface(
                    onClick = { mainNavigator.push(MainScreen.Settings) },
                    modifier = Modifier.fillMaxHeight(),
                    shape = SmoothRoundedCornerShape(16.dp),
                    colorToken = Theme.colorScheme.surfaceContainerHigh,
                    contentAlignment = Alignment.Center,
                    propagateMinConstraints = false
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Settings)
                        Text(text = "Settings")
                    }
                }
            }
        }
        Text(
            text = "Library",
            modifier = Modifier
                .padding(24.dp, 0.dp, 16.dp, 8.dp),
            style = Theme.typography.titleLarge
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Surface(
                onClick = { push(LibraryScreen.Songs) },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = SmoothRoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        text = "Songs",
                        style = Theme.typography.bodyLarge
                    )
                }
            }
            Surface(
                onClick = { push(LibraryScreen.Albums) },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = SmoothRoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        text = "Albums",
                        style = Theme.typography.bodyLarge
                    )
                }
            }
            Surface(
                onClick = { push(LibraryScreen.Artists) },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = SmoothRoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        text = "Artists",
                        style = Theme.typography.bodyLarge
                    )
                }
            }
            Surface(
                onClick = { push(LibraryScreen.Genres) },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = SmoothRoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        text = "Genres",
                        style = Theme.typography.bodyLarge
                    )
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            Surface(
                onClick = { scope.launch(Dispatchers.IO) { MediaStore.scan(context) } },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                shape = SmoothRoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 16.dp)
                ) {
                    Text(
                        text = "Refresh",
                        style = Theme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
