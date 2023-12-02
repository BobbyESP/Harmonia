package com.kyant.music.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.adaptive.collectWindowSizeAsState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.AppScreen
import com.kyant.music.ui.style.valueToken
import com.kyant.music.util.AsyncImage
import com.kyant.ui.BoxNoInline
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.navigation.OnBackPressed
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.motion.Duration
import com.kyant.ui.style.motion.Easing
import com.kyant.ui.style.motion.Easing.with
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlin.math.roundToInt

@Composable
fun MusicLibrary() {
    val navigator = currentNavigator<AppScreen>()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp)
    ) {
        val scope = rememberCoroutineScope()
        val libraryNavigator = remember(constraints) {
            LibraryNavigator(scope, constraints.maxWidth.toFloat())
        }

        val windowAdaptiveInfo = currentWindowAdaptiveInfo()
        if (windowAdaptiveInfo.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
            val size by collectWindowSizeAsState()
            BoxNoInline(
                modifier = Modifier
                    .fillMaxSize()
                    .draggable(
                        state = libraryNavigator.draggableState,
                        orientation = Orientation.Horizontal,
                        onDragStopped = { velocity -> libraryNavigator.fling(velocity) },
                        reverseDirection = true
                    )
            ) {
                BoxNoInline(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(constraints.maxWidth, constraints.maxHeight) {
                                placeable.placeRelative(
                                    (
                                        (0 - libraryNavigator.paneExpandProgressValue) * size.width
                                        ).roundToInt(),
                                    0
                                )
                            }
                        }
                        .graphicsLayer {
                            alpha = lerp(1f, 0f, libraryNavigator.paneExpandProgressValue)
                        }
                ) {
                    libraryNavigator.Home(navigator = navigator)
                }

                BoxNoInline(
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelative(
                                (
                                    (1 - libraryNavigator.paneExpandProgressValue) * size.width
                                    ).roundToInt(),
                                0
                            )
                        }
                    }
                ) {
                    with(libraryNavigator) {
                        when (listPaneRoute) {
                            ListPaneRoute.Songs -> Songs()
                            ListPaneRoute.Albums -> Albums()
                            else -> {}
                        }
                    }
                }

                BoxNoInline {
                    IconButton(
                        onClick = {
                            if (libraryNavigator.targetPaneExpandProgress < 0.5f) {
                                libraryNavigator.expandPane()
                            } else {
                                libraryNavigator.collapsePane()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .graphicsLayer {
                                translationX = lerp(
                                    size.width - 72.dp.toPx(),
                                    0f,
                                    libraryNavigator.paneExpandProgressValue
                                )
                                translationY = 40.dp.toPx()
                            }
                    ) {
                        if (libraryNavigator.targetPaneExpandProgress < 0.5f) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close menu"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.MenuOpen,
                                contentDescription = "Open menu"
                            )
                        }
                    }
                }

                BoxNoInline(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = valueToken.safeBottomPadding)
                ) {
                    Surface(
                        shape = Rounding.Full.asSmoothRoundedShape(),
                        colorSet = colorScheme.secondaryContainer
                    ) {
                        val player = LocalPlayer.current
                        val song = remember(player.currentMediaItem) {
                            mediaStore.getSong(player.currentMediaItem?.mediaId)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            song?.let { song ->
                                Box(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        onClick = { player.playFromMediaId(song.mediaId) },
                                        shape = Rounding.Small.asSmoothRoundedShape(),
                                        colorSet = colorScheme.secondaryFixedDim
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(8.dp, 8.dp, 16.dp, 8.dp)
                                                .animateContentSize(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = song.thumbnailUri,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(Rounding.ExtraSmall.asSmoothRoundedShape())
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                SingleLineText(
                                                    text = song.title,
                                                    style = typography.bodyMedium
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = 6.dp)
                                                        .size(2.5.dp)
                                                        .clip(Rounding.Full.asRoundedShape())
                                                        .background(LocalColorSet.current.onColor)
                                                )
                                                SingleLineText(
                                                    text = song.displayArtist,
                                                    emphasis = 0.6f,
                                                    style = typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { player.skipToPrevious() }) {
                                        Icon(
                                            painter = painterResource(id = com.kyant.media.R.drawable.skip_previous),
                                            contentDescription = "Previous"
                                        )
                                    }
                                    IconButton(onClick = {
                                        if (player.isPlaying) {
                                            player.pause()
                                        } else {
                                            player.play()
                                        }
                                    }) {
                                        if (player.isPlaying) {
                                            Icon(
                                                painter = painterResource(id = com.kyant.media.R.drawable.pause),
                                                contentDescription = "Pause"
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = com.kyant.media.R.drawable.play),
                                                contentDescription = "Play"
                                            )
                                        }
                                    }
                                    IconButton(onClick = { player.skipToNext() }) {
                                        Icon(
                                            painter = painterResource(id = com.kyant.media.R.drawable.skip_next),
                                            contentDescription = "Next"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val isVerticallyFoldable = remember(windowAdaptiveInfo) {
                windowAdaptiveInfo.windowPosture.separatingVerticalHingeBounds.isNotEmpty()
            }
            val hingeWidth = if (isVerticallyFoldable) {
                windowAdaptiveInfo.windowPosture.separatingVerticalHingeBounds.first().width +
                    with(LocalDensity.current) { 24.dp.toPx() }
            } else {
                with(LocalDensity.current) { 24.dp.toPx() }
            }
            val separatedFraction = if (isVerticallyFoldable) 0.5f else 1f / 3f

            BoxNoInline(
                modifier = Modifier.layout { measurable, constraints ->
                    val fraction = lerp(1f, separatedFraction, libraryNavigator.paneExpandProgressValue)
                    val maxWidth =
                        (fraction * libraryNavigator.width - (1f - fraction) * hingeWidth).roundToInt()
                    val placeable = measurable.measure(constraints.copy(maxWidth = maxWidth))
                    layout(maxWidth, constraints.maxHeight) {
                        placeable.placeRelative(0, 0)
                    }
                }
            ) {
                libraryNavigator.Home(navigator = navigator)
            }

            AnimatedVisibility(
                visible = libraryNavigator.targetPaneExpandProgress == 1,
                modifier = Modifier.layout { measurable, constraints ->
                    val fraction = 1f - lerp(1f, separatedFraction, libraryNavigator.paneExpandProgressValue)
                    val endMaxWidth = ((1f - separatedFraction) * (libraryNavigator.width - hingeWidth)).roundToInt()
                    val paddingStart =
                        ((1f - fraction) * libraryNavigator.width + fraction * hingeWidth).roundToInt()
                    val placeable = measurable.measure(constraints.copy(maxWidth = endMaxWidth))
                    layout(endMaxWidth, constraints.maxHeight) {
                        placeable.placeRelative(paddingStart, 0)
                    }
                },
                enter = fadeIn(Easing.EmphasizedAccelerate with Duration.SHORT_4),
                exit = fadeOut(Easing.EmphasizedDecelerate with Duration.SHORT_4)
            ) {
                with(libraryNavigator) {
                    when (listPaneRoute) {
                        ListPaneRoute.Songs -> Songs()
                        ListPaneRoute.Albums -> Albums()
                        else -> {}
                    }
                }
            }
            OnBackPressed(enabled = { libraryNavigator.targetPaneExpandProgress == 1 }) {
                libraryNavigator.collapsePane()
            }
        }
    }
}
