package com.kyant.music.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.core.MediaItem
import com.kyant.music.util.AsyncImage
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.util.thenIf

@Composable
fun LibraryNavigator.Songs(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Headline(
            text = "Songs",
            modifier = Modifier.padding(top = 24.dp)
        )

        val isCompact = DeviceSpecs.isCompact

        Surface(
            modifier = Modifier.thenIf(isCompact) {
                graphicsLayer {
                    translationX = (1f - paneExpandProgressValue) * 72.dp.toPx()
                }
            },
            shape = Rounding.Full.asSmoothRoundedShape(),
            colorSet = colorScheme.surfaceContainerHigh
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable {},
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        size = 20.dp
                    )
                    Text(text = "Shuffle all")
                }
            }
        }

        val player = LocalPlayer.current
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .thenIf(isCompact) {
                    graphicsLayer {
                        translationX = (1f - paneExpandProgressValue) * 36.dp.toPx()
                    }
                }
                .clip(Rounding.Large.asSmoothRoundedShape()),
            state = state,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(mediaStore.songs, { song -> song.mediaId }) { song ->
                MediaItem(
                    onClick = { player.playFromMediaId(song.mediaId) },
                    image = { AsyncImage(model = song.thumbnailUri) },
                    title = song.title,
                    subtitle = song.displayArtist
                ) {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.MoreVert)
                    }
                }
            }
        }
    }
}
