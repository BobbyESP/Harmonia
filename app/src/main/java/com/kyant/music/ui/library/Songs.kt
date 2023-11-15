package com.kyant.music.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.util.AsyncImage
import com.kyant.music.util.hazeBlur
import com.kyant.music.util.plus
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalOverscroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun Songs() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val density = LocalDensity.current
        var titleBarHeight by remember { mutableFloatStateOf(0f) }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val state = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .hazeBlur(
                        Rect(
                            0f,
                            0f,
                            this@BoxWithConstraints.constraints.maxWidth.toFloat(),
                            titleBarHeight
                        )
                    )
                    .smoothVerticalOverscroll(state),
                state = state,
                contentPadding = PaddingValues(top = with(density) { titleBarHeight.toDp() }) +
                    WindowInsets.navigationBars.asPaddingValues()
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
        Text(
            text = "Songs",
            modifier = Modifier
                .onSizeChanged { titleBarHeight = it.height.toFloat() }
                .statusBarsPadding()
                .padding(24.dp, 24.dp, 16.dp, 24.dp),
            style = Theme.typography.titleLarge
        )
    }
}
