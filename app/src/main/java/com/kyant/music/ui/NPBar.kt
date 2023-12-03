package com.kyant.music.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.media.R
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.style.DynamicTheme
import com.kyant.music.util.AsyncImage
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography

@Composable
fun NPBar(modifier: Modifier = Modifier) {
    val player = LocalPlayer.current
    val song = remember(player.currentMediaItem) {
        mediaStore.getSong(player.currentMediaItem?.mediaId)
    }
    DynamicTheme(song = song) {
        Surface(
            modifier = modifier,
            shape = Rounding.Full.asSmoothRoundedShape(),
            colorSet = colorScheme.secondaryContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = song?.thumbnailUri,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(Rounding.ExtraSmall.asSmoothRoundedShape())
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SingleLineText(
                            text = song?.title ?: "No playing",
                            style = typography.bodyMedium
                        )
                        song?.displayArtist?.let { artist ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(2.5.dp)
                                    .clip(Rounding.Full.asRoundedShape())
                                    .background(LocalColorSet.current.onColor)
                            )
                            SingleLineText(
                                text = artist,
                                emphasis = 0.6f,
                                style = typography.bodyMedium
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { player.skipToPrevious() },
                        size = 32.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.skip_previous),
                            contentDescription = "Previous"
                        )
                    }
                    IconButton(
                        onClick = {
                            if (player.isPlaying) {
                                player.pause()
                            } else {
                                player.play()
                            }
                        },
                        size = 32.dp
                    ) {
                        if (player.isPlaying) {
                            Icon(
                                painter = painterResource(id = R.drawable.pause),
                                contentDescription = "Pause"
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.play),
                                contentDescription = "Play"
                            )
                        }
                    }
                    IconButton(
                        onClick = { player.skipToNext() },
                        size = 32.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.skip_next),
                            contentDescription = "Next"
                        )
                    }
                }
            }
        }
    }
}
