package com.kyant.music.ui.library

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import com.kyant.music.ui.AppScreen
import com.kyant.ui.BoxNoInline
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.util.lerp
import kotlin.math.roundToInt

@Composable
fun MusicLibrary() {
    val navigator = currentNavigator<AppScreen>()
    val libraryNavigator = rememberLibraryNavigator()

    BoxNoInline(
        modifier = Modifier.draggable(
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
                            ((0 - libraryNavigator.paneExpandProgressValue) * constraints.maxWidth).roundToInt(),
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
                        ((1 - libraryNavigator.paneExpandProgressValue) * constraints.maxWidth).roundToInt(),
                        0
                    )
                }
            }
        ) {
            libraryNavigator.Songs()
        }
    }
}
