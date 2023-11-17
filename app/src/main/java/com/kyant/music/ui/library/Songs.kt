package com.kyant.music.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.outlined.Category
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.util.AsyncImage
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalOverscroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun LibraryScreen.Companion.Controller.Songs() {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier
                .pointerInput(Unit) { detectTapGestures() }
                .statusBarsPadding()
                .padding(8.dp, 8.dp, 16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isMenuOpen = true }) {
                Icon(imageVector = Icons.Outlined.Category)
            }
            Text(
                text = "Songs",
                style = Theme.typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.AutoMirrored.Default.Sort)
                }
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(id = com.kyant.media.R.drawable.shuffle))
                }
            }
        }

        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .smoothVerticalOverscroll(state),
            state = state,
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
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
            }
        }
    }
}
