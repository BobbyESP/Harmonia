package com.kyant.music.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.outlined.Category
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.MediaStore
import com.kyant.music.ui.theme.isDark
import com.kyant.music.util.AsyncImage
import com.kyant.music.util.hazeBlur
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.LocalIconSize
import com.kyant.ui.ProvideTextStyle
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalOverscroll
import com.kyant.ui.graphics.SmoothRoundedCornerShape
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.typography
import com.kyant.ui.util.thenIf

@Composable
fun LibraryScreen.Companion.Controller.Songs() {
    var topBarRect by remember { mutableStateOf(Rect.Zero) }

    var multiSelect by rememberSaveable { mutableStateOf(false) }
    val selectedSongIds = remember { mutableStateListOf<String>() }
    var multiSelectBarRect by remember { mutableStateOf(Rect.Zero) }

    Box {
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .hazeBlur(
                    topBarRect,
                    multiSelectBarRect
                )
                .smoothVerticalOverscroll(state),
            state = state
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .statusBarsPadding()
                        .height(56.dp)
                )
            }
            items(MediaStore.songs, { song -> song.mediaId }) { song ->
                val selected by remember { derivedStateOf { song.mediaId in selectedSongIds } }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (multiSelect) {
                                if (selected) {
                                    selectedSongIds.remove(song.mediaId)
                                } else {
                                    selectedSongIds.add(song.mediaId)
                                }
                            }
                        }
                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val thumbnailSize by animateDpAsState(
                            targetValue = if (multiSelect) {
                                if (selected) 48.dp else 40.dp
                            } else {
                                48.dp
                            }
                        )
                        val thumbnailAlpha by animateFloatAsState(
                            targetValue = if (multiSelect) {
                                if (selected) 1f else 0.6f
                            } else {
                                1f
                            }
                        )
                        AsyncImage(
                            model = song.thumbnailUri,
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = thumbnailAlpha
                                    (thumbnailSize.toPx() / 48.dp.toPx()).let {
                                        scaleX = it
                                        scaleY = it
                                    }
                                }
                                .clip(SmoothRoundedCornerShape(8.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = song.title,
                            style = typography.bodyLarge
                        )
                        Text(
                            text = song.displayArtist,
                            emphasis = if (isDark) 0.6f else 0.5f,
                            style = typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            emphasis = 0.5f
                        )
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .thenIf(multiSelect) { height(64.dp) }
                )
            }
        }

        Row(
            modifier = Modifier
                .onGloballyPositioned { topBarRect = it.boundsInParent() }
                .pointerInput(Unit) { detectTapGestures() }
                .statusBarsPadding()
                .height(56.dp)
                .padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isMenuOpen = true }) {
                Icon(imageVector = Icons.Outlined.Category)
            }
            Text(
                text = "Songs",
                style = typography.titleLarge
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    IconButton(onClick = { multiSelect = !multiSelect }) {
                        Icon(imageVector = Icons.Default.ChecklistRtl)
                    }
                    if (multiSelect) {
                        Text(
                            text = selectedSongIds.size.toString(),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(colorScheme.primary.color)
                                .padding(horizontal = 8.dp),
                            color = colorScheme.primary.onColor,
                            style = typography.labelMedium
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.AutoMirrored.Default.Sort)
                }
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(id = com.kyant.media.R.drawable.shuffle))
                }
            }
        }

        AnimatedVisibility(
            visible = multiSelect,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { multiSelectBarRect = it.boundsInParent() }
                .pointerInput(Unit) { detectTapGestures() },
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            DisposableEffect(Unit) {
                onDispose {
                    multiSelectBarRect = Rect.Zero
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                ProvideTextStyle(value = typography.bodyMedium) {
                    CompositionLocalProvider(value = LocalIconSize provides 20.dp) {
                        val colorToken = colorScheme.surface.copy(
                            color = Color.Transparent
                        )
                        Surface(
                            onClick = {
                                selectedSongIds.clear()
                                selectedSongIds.addAll(MediaStore.songs.map { it.mediaId })
                            },
                            modifier = Modifier.weight(1f),
                            shape = SmoothRoundedCornerShape(16.dp),
                            colorToken = colorToken
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp, 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.SelectAll)
                                Text(text = "Select all")
                            }
                        }
                        Surface(
                            onClick = {
                                multiSelect = false
                                selectedSongIds.clear()
                            },
                            modifier = Modifier.weight(1f),
                            shape = SmoothRoundedCornerShape(16.dp),
                            colorToken = colorToken
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp, 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Close)
                                Text(text = "Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}
