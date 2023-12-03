package com.kyant.music.ui.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kyant.music.service.LocalPlayer
import com.kyant.music.ui.style.DynamicTheme
import com.kyant.music.util.AsyncImage
import com.kyant.ui.BoxNoInline
import com.kyant.ui.FullScreenDialog
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography

@Composable
fun LibraryState.SongDialog() {
    FullScreenDialog(
        visible = hasSelectedSong,
        onDismissRequest = { selectedSong = null }
    ) {
        val song by produceState(initialValue = selectedSong, selectedSong) {
            if (selectedSong != null) {
                value = selectedSong
            }
        }
        DynamicTheme(song = song) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            selectedSong = null
                        }
                    },
                colorSet = colorScheme.secondaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = song?.thumbnailUri,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(Rounding.Small.asSmoothRoundedShape())
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                SingleLineText(
                                    text = song?.title ?: "Unknown Title",
                                    style = typography.titleLarge
                                )
                                SingleLineText(
                                    text = song?.displayArtist ?: "Unknown Artist",
                                    emphasis = 0.6f,
                                    style = typography.titleLarge
                                )
                            }
                        }
                    }

                    Column {
                        val player = LocalPlayer.current

                        val isPlaying by remember {
                            derivedStateOf {
                                player.isPlaying && player.currentMediaItem?.mediaId == song?.mediaId
                            }
                        }
                        BoxNoInline(
                            modifier = Modifier.clickable {
                                if (isPlaying) {
                                    player.pause()
                                } else {
                                    player.playFromMediaId(song?.mediaId)
                                }
                            }
                        ) {
                            AnimatedContent(targetState = isPlaying) { isPlaying ->
                                if (isPlaying) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pause",
                                            style = typography.titleLarge
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Play",
                                            style = typography.titleLarge
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp),
                            thickness = 2.dp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {}
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add to Queue",
                                style = typography.titleLarge
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 8.dp),
                            thickness = 2.dp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {}
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Metadata",
                                style = typography.titleLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
