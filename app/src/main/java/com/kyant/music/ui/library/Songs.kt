package com.kyant.music.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.util.AsyncImage
import com.kyant.music.util.hazeBlur
import com.kyant.music.util.plus
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalOverscroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.theme.Theme

@Composable
fun Songs() {
    val density = LocalDensity.current
    var titleBarRect by remember { mutableStateOf(Rect.Zero) }
    var isSortMenuVisible by remember { mutableStateOf(false) }
    var sortMenuRect by remember { mutableStateOf(Rect.Zero) }

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .hazeBlur(
                    RoundRect(sortMenuRect, with(density) { 24.dp.toPx() }, with(density) { 24.dp.toPx() }),
                    backgroundColor = Theme.colorScheme.surfaceContainerHighest.color
                )
        ) {
            val state = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .hazeBlur(titleBarRect)
                    .smoothVerticalOverscroll(state),
                state = state,
                contentPadding = PaddingValues(top = with(density) { titleBarRect.height.toDp() }) +
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
    }
    Column {
        Row(
            modifier = Modifier
                .onGloballyPositioned { titleBarRect = it.boundsInParent() }
                .pointerInput(Unit) { detectTapGestures() }
                .statusBarsPadding()
                .padding(24.dp, 16.dp, 16.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Songs",
                style = Theme.typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { isSortMenuVisible = !isSortMenuVisible }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Sort)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = com.kyant.media.R.drawable.shuffle))
                }
            }
        }
        AnimatedVisibility(
            visible = isSortMenuVisible,
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
                .onGloballyPositioned { sortMenuRect = it.boundsInParent() }
                .pointerInput(Unit) { detectTapGestures() }
        ) {
            DisposableEffect(Unit) {
                onDispose {
                    sortMenuRect = Rect.Zero
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
            }
        }
    }
}
