package com.kyant.music.ui.library

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.NPBar
import com.kyant.music.ui.style.valueToken
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.BoxNoInline
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.util.lerp
import kotlin.math.roundToInt

@Composable
fun MusicLibrary() {
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

        if (DeviceSpecs.isCompact) {
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
                val size = DeviceSpecs.size
                BoxNoInline(
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelativeWithLayer(
                                ((0 - libraryNavigator.paneExpandProgressValue) * size.width).roundToInt(),
                                0
                            ) {
                                alpha = lerp(1f, 0f, libraryNavigator.paneExpandProgressValue)
                            }
                        }
                    }
                ) {
                    libraryNavigator.LibraryMenu()
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BoxNoInline(
                        modifier = Modifier
                            .weight(1f)
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(constraints.maxWidth, constraints.maxHeight) {
                                    if (libraryNavigator.paneExpandProgressValue > 0f) {
                                        placeable.placeRelative(
                                            ((1 - libraryNavigator.paneExpandProgressValue) * size.width).roundToInt(),
                                            0
                                        )
                                    }
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

                    NPBar(
                        modifier = Modifier.padding(bottom = valueToken.safeBottomPadding.value)
                    )
                }

                IconButton(
                    onClick = {
                        if (libraryNavigator.targetPaneExpandProgress < 0.5f) {
                            libraryNavigator.expandPane()
                        } else {
                            libraryNavigator.collapsePane()
                        }
                    },
                    modifier = Modifier.graphicsLayer {
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
        } else {
            val separatedFraction = if (DeviceSpecs.isVerticallyFoldable) 0.5f else 1f / 3f

            Row(
                horizontalArrangement = Arrangement.spacedBy(DeviceSpecs.hingeWidth + 24.dp)
            ) {
                BoxNoInline(
                    modifier = Modifier.fillMaxWidth(separatedFraction)
                ) {
                    libraryNavigator.LibraryMenu()
                }

                BoxNoInline {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BoxNoInline(
                            modifier = Modifier.weight(1f)
                        ) {
                            with(libraryNavigator) {
                                when (listPaneRoute) {
                                    ListPaneRoute.Songs -> Songs()
                                    ListPaneRoute.Albums -> Albums()
                                    else -> {}
                                }
                            }
                        }

                        NPBar(
                            modifier = Modifier.padding(bottom = valueToken.safeBottomPadding.value)
                        )
                    }
                }
            }
        }
    }
}
