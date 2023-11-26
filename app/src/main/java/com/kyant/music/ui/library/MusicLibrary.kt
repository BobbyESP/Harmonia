package com.kyant.music.ui.library

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.MediaStore
import com.kyant.music.ui.AppScreen
import com.kyant.music.ui.theme.isDark
import com.kyant.music.util.AsyncImage
import com.kyant.ui.BoxNoInline
import com.kyant.ui.FilledTonalButton
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun MusicLibrary() {
    val navigator = currentNavigator<AppScreen>()
    BoxWithConstraints {
        val width = constraints.maxWidth

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val scope = rememberCoroutineScope()
            val page = remember { Animatable(1f) }
            val targetPage by remember {
                derivedStateOf {
                    page.targetValue.roundToInt()
                }
            }

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BoxNoInline {
                    SingleLineText(
                        text = when (targetPage) {
                            0 -> "Music Library"
                            1 -> "Songs"
                            else -> ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .graphicsLayer {
                                translationX = -(page.value - targetPage) * width / 2f
                            },
                        color = colorScheme.primary.color,
                        emphasis = lerp(0.8f, 0.2f, (page.value - targetPage).absoluteValue * 2f),
                        textAlign = TextAlign.Center,
                        style = typography.headlineLarge
                    )
                }
            }

            if (MediaStore.songs.isEmpty()) {
                Surface(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    shape = Rounding.Large.asSmoothRoundedShape()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Scan your library",
                            modifier = Modifier.padding(16.dp, 8.dp),
                            style = typography.headlineSmall
                        )
                        val context = LocalContext.current
                        FilledTonalButton(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    MediaStore.scan(context)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            enabled = !MediaStore.isScanning
                        ) {
                            Text(text = "Scan")
                        }
                    }
                }
            }

            val draggableState = rememberDraggableState {
                scope.launch {
                    page.snapTo((page.value + it / width).coerceIn(0f..1f))
                }
            }
            BoxNoInline(
                modifier = Modifier.draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        scope.launch {
                            when {
                                velocity > 0 -> page.animateTo(
                                    targetValue = (page.value.toInt() + 1f).coerceAtMost(1f),
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        visibilityThreshold = 0.0001f
                                    ),
                                    initialVelocity = velocity / width
                                )

                                velocity < 0 -> page.animateTo(
                                    targetValue = page.value.toInt().toFloat().coerceAtLeast(0f),
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        visibilityThreshold = 0.0001f
                                    ),
                                    initialVelocity = velocity / width
                                )

                                else -> page.animateTo(
                                    targetValue = page.value.roundToInt().toFloat().coerceIn(0f..1f),
                                    animationSpec = spring(
                                        stiffness = Spring.StiffnessLow,
                                        visibilityThreshold = 0.0001f
                                    ),
                                    initialVelocity = 0f
                                )
                            }
                        }
                    },
                    reverseDirection = true
                )
            ) {
                BoxNoInline(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(constraints.maxWidth, constraints.maxHeight) {
                                placeable.placeRelative(((0 - page.value) * constraints.maxWidth).roundToInt(), 0)
                            }
                        }
                        .graphicsLayer {
                            alpha = lerp(1f, 0f, page.value)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .verticalScroll(rememberScrollState())
                            .clip(Rounding.Large.asSmoothRoundedShape()),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Surface(
                            onClick = {
                                scope.launch {
                                    page.animateTo(
                                        1f,
                                        animationSpec = spring(
                                            stiffness = Spring.StiffnessLow,
                                            visibilityThreshold = 0.0001f
                                        )
                                    )
                                }
                            },
                            shape = Rounding.ExtraSmall.asRoundedShape(),
                            colorSet = colorScheme.surfaceContainerLowest
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp, 8.dp, 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {}
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Songs",
                                        style = typography.bodyLarge
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.NavigateNext,
                                    emphasis = 0.5f
                                )
                            }
                        }
                        Surface(
                            onClick = { navigator.push(AppScreen.Settings) },
                            shape = Rounding.ExtraSmall.asRoundedShape(),
                            colorSet = colorScheme.surfaceContainerLowest
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp, 8.dp, 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {}
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Settings",
                                        style = typography.bodyLarge
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.NavigateNext,
                                    emphasis = 0.5f
                                )
                            }
                        }
                    }
                }

                BoxNoInline(
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelative(((1 - page.value) * constraints.maxWidth).roundToInt(), 0)
                        }
                    }
                ) {
                    val player = LocalPlayer.current
                    val state = rememberLazyListState()

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(Rounding.Large.asSmoothRoundedShape()),
                        state = state,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(MediaStore.songs, { song -> song.mediaId }) { song ->
                            Surface(
                                onClick = { player.playFromMediaId(song.mediaId) },
                                shape = Rounding.ExtraSmall.asRoundedShape(),
                                colorSet = colorScheme.surfaceContainerLowest
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(48.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = song.thumbnailUri,
                                            modifier = Modifier.clip(Rounding.Small.asSmoothRoundedShape())
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
                        }
                    }
                }
            }
        }
    }
}
