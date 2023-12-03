package com.kyant.music.ui.library

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.NPSheet
import com.kyant.music.ui.NpSheetState
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.BoxNoInline
import com.kyant.ui.util.lerp
import kotlinx.coroutines.launch

@Composable
fun MusicLibrary() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp)
            .graphicsLayer {
                if (NpSheetState.expanded) {
                    alpha = 0f
                }
            }
    ) {
        with(LibraryNavigator) {
            if (DeviceSpecs.isCompact) {
                val scope = rememberCoroutineScope()
                BoxNoInline(
                    modifier = Modifier
                        .fillMaxSize()
                        .draggable(
                            state = remember(constraints) {
                                draggableState(scope, constraints.maxWidth.toFloat())
                            },
                            orientation = Orientation.Horizontal,
                            onDragStopped = { velocity ->
                                scope.launch {
                                    fling(velocity, constraints.maxWidth.toFloat())
                                }
                            },
                            reverseDirection = true
                        )
                ) {
                    val screenWidth = DeviceSpecs.size.width

                    LibraryMenu(
                        modifier = Modifier.graphicsLayer {
                            translationX = -paneExpandProgressValue * screenWidth
                            alpha = 1f - paneExpandProgressValue
                        }
                    )

                    LibraryContent(
                        modifier = Modifier.graphicsLayer {
                            translationX = (1f - paneExpandProgressValue) * screenWidth
                            if (paneExpandProgressValue < 0f) {
                                alpha = 0f
                            }
                        }
                    )

                    LibraryMenuButton(
                        modifier = Modifier.graphicsLayer {
                            translationX = lerp(
                                screenWidth - 72.dp.toPx(),
                                0f,
                                paneExpandProgressValue
                            )
                            translationY = 40.dp.toPx()
                        }
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DeviceSpecs.hingeWidth + 24.dp)
                ) {
                    LibraryMenu(
                        modifier = Modifier.fillMaxWidth(if (DeviceSpecs.isVerticallyFoldable) 0.5f else 1f / 3f)
                    )

                    LibraryContent()
                }
            }
        }
    }

    NPSheet()
}
