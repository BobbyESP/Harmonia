package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.style.colorToken
import com.kyant.music.util.AsyncImage
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun LibraryNavigator.Albums(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SingleLineText(
                text = "Albums",
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

        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .clip(Rounding.Large.asSmoothRoundedShape()),
            state = state,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(mediaStore.albums, { album -> album.mediaId }) { album ->
                Surface(
                    onClick = {},
                    shape = Rounding.ExtraSmall.asRoundedShape(),
                    colorSet = colorToken.card
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp, 8.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = album.thumbnailUri,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(Rounding.Small.asSmoothRoundedShape())
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = album.title,
                                style = typography.bodyLarge
                            )
                            Text(
                                text = album.displayAlbumArtist,
                                emphasis = 0.6f,
                                style = typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More for ${album.title}",
                                emphasis = 0.6f
                            )
                        }
                    }
                }
            }
        }
    }
}
