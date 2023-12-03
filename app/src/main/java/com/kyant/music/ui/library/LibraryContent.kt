package com.kyant.music.ui.library

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.NPBar
import com.kyant.music.ui.NpSheetState
import com.kyant.music.ui.style.valueToken
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.BoxNoInline
import kotlinx.coroutines.launch

@Composable
fun LibraryState.LibraryContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BoxNoInline(
            modifier = Modifier.weight(1f)
        ) {
            when (listPaneRoute) {
                ListPaneRoute.Songs -> Songs()
                ListPaneRoute.Albums -> Albums()
                else -> {}
            }
        }

        val scope = rememberCoroutineScope()
        val screenHeight = DeviceSpecs.size.height
        NPBar(
            modifier = Modifier
                .padding(bottom = valueToken.safeBottomPadding.value)
                .pointerInput(Unit) {
                    detectTapGestures {
                        scope.launch {
                            NpSheetState.expandPane()
                        }
                    }
                }
                .draggable(
                    state = remember(screenHeight) {
                        NpSheetState.draggableState(scope, screenHeight.toFloat())
                    },
                    orientation = Orientation.Vertical,
                    onDragStopped = { velocity ->
                        scope.launch {
                            NpSheetState.fling(velocity, screenHeight.toFloat())
                        }
                    },
                    reverseDirection = true
                )
        )
    }
}
