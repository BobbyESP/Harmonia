package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
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
import com.kyant.ui.ProvideTextStyle
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalScroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.theme.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen.Companion.Controller.Library(navigator: Navigator<MainScreen>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .smoothVerticalScroll(rememberScrollState())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Library",
            modifier = Modifier.padding(24.dp, 24.dp, 16.dp, 8.dp),
            style = Theme.typography.titleLarge
        )
        ProvideTextStyle(value = Theme.typography.bodyLarge) {
            CompositionLocalProvider(value = LocalIconSize provides 20.dp) {
                val colorToken = Theme.colorScheme.surface.copy(
                    color = Theme.colorScheme.surface.color.copy(alpha = 0.5f)
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        onClick = { changeTo(LibraryScreen.Songs) },
                        modifier = Modifier.weight(1f),
                        shape = SmoothRoundedCornerShape(16.dp),
                        colorToken = colorToken
                    ) {
                        Column(
                            modifier = Modifier.width(160.dp).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MusicNote)
                            Text(text = "Songs")
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.Albums) },
                        modifier = Modifier.weight(1f),
                        shape = SmoothRoundedCornerShape(16.dp),
                        colorToken = colorToken
                    ) {
                        Column(
                            modifier = Modifier.width(160.dp).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Album)
                            Text(text = "Albums")
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.Artists) },
                        modifier = Modifier.weight(1f),
                        shape = SmoothRoundedCornerShape(16.dp),
                        colorToken = colorToken
                    ) {
                        Column(
                            modifier = Modifier.width(160.dp).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PeopleAlt)
                            Text(text = "Artists")
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.Genres) },
                        modifier = Modifier.weight(1f),
                        shape = SmoothRoundedCornerShape(16.dp),
                        colorToken = colorToken
                    ) {
                        Column(
                            modifier = Modifier.width(160.dp).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Category)
                            Text(text = "Genres")
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.FolderView) },
                        modifier = Modifier.weight(1f),
                        shape = SmoothRoundedCornerShape(16.dp),
                        colorToken = colorToken
                    ) {
                        Column(
                            modifier = Modifier.width(160.dp).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.FolderOpen)
                            Text(text = "Folder view")
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Surface(
                        onClick = { changeTo(LibraryScreen.PlayQueue) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = SmoothRoundedCornerShape(8.dp),
                        colorToken = colorToken
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Default.QueueMusic)
                            Text(
                                text = "Play queue",
                                style = Theme.typography.bodyLarge
                            )
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.Favorites) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = SmoothRoundedCornerShape(8.dp),
                        colorToken = colorToken
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Stars)
                            Text(
                                text = "Favorites",
                                style = Theme.typography.bodyLarge
                            )
                        }
                    }
                    Surface(
                        onClick = { changeTo(LibraryScreen.Playlists) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = SmoothRoundedCornerShape(8.dp),
                        colorToken = colorToken
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CollectionsBookmark)
                            Text(
                                text = "Playlists",
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
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = SmoothRoundedCornerShape(8.dp),
                        colorToken = colorToken
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Refresh)
                            Text(
                                text = "Refresh",
                                style = Theme.typography.bodyLarge
                            )
                        }
                    }
                    Surface(
                        onClick = { navigator.push(MainScreen.Settings) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = SmoothRoundedCornerShape(8.dp),
                        colorToken = colorToken
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Settings)
                            Text(
                                text = "Settings",
                                style = Theme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
