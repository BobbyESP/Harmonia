package com.kyant.music.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.util.AsyncImage
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalOverscroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun Songs() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Songs",
            modifier = Modifier.padding(24.dp, 24.dp, 16.dp, 8.dp),
            style = Theme.typography.titleLarge
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            val state = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .smoothVerticalOverscroll(state),
                state = state
            ) {
                items(MediaStore.songs, { song -> song.mediaId }) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {}
                            .padding(16.dp, 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = song.thumbnailUri,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(SmoothRoundedCornerShape(8.dp))
                        )
                        Column {
                            Text(
                                text = song.title,
                                style = Theme.typography.bodyLarge
                            )
                            Text(
                                text = song.displayArtist,
                                emphasis = 0.6f,
                                style = Theme.typography.bodyLarge
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
                }
            }
        }
    }
}
