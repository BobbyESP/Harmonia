package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.core.MediaItem
import com.kyant.music.util.AsyncImage
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.style.shape.Rounding

@Composable
fun LibraryNavigator.Albums(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Headline(
            text = "Albums",
            modifier = Modifier.padding(top = 24.dp)
        )

        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .clip(Rounding.Large.asSmoothRoundedShape()),
            state = state,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(mediaStore.albums, { album -> album.mediaId }) { album ->
                MediaItem(
                    onClick = {},
                    image = { AsyncImage(model = album.thumbnailUri) },
                    title = album.title,
                    subtitle = album.displayAlbumArtist
                ) {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.AutoMirrored.Default.NavigateNext)
                    }
                }
            }
        }
    }
}
