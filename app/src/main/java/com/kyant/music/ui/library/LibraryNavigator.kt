package com.kyant.music.ui.library

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Stable
class LibraryNavigator(
    private val scope: CoroutineScope,
    val width: Float = 0f
) {
    var listPaneRoute by mutableStateOf(ListPaneRoute.Songs)
        private set

    private val paneExpandProgress = Animatable(1f)

    val paneExpandProgressValue by derivedStateOf {
        paneExpandProgress.value
    }

    val targetPaneExpandProgress by derivedStateOf {
        paneExpandProgress.targetValue.roundToInt()
    }

    val draggableState = DraggableState {
        scope.launch {
            paneExpandProgress.snapTo((paneExpandProgress.value + it / width).coerceIn(0f..1f))
        }
    }

    fun navigate(route: ListPaneRoute) {
        listPaneRoute = route
        expandPane()
    }

    fun expandPane(initialVelocity: Float = 0f) {
        scope.launch {
            paneExpandProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = initialVelocity
            )
        }
    }

    fun collapsePane(initialVelocity: Float = 0f) {
        scope.launch {
            paneExpandProgress.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = initialVelocity
            )
        }
    }

    fun fling(velocity: Float) {
        scope.launch {
            when {
                velocity > 0 -> paneExpandProgress.animateTo(
                    targetValue = (paneExpandProgress.value.toInt() + 1f).coerceAtMost(1f),
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        visibilityThreshold = 0.0001f
                    ),
                    initialVelocity = velocity / width
                )

                velocity < 0 -> paneExpandProgress.animateTo(
                    targetValue = paneExpandProgress.value.toInt().toFloat().coerceAtLeast(0f),
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        visibilityThreshold = 0.0001f
                    ),
                    initialVelocity = velocity / width
                )

                else -> paneExpandProgress.animateTo(
                    targetValue = paneExpandProgress.value.roundToInt().toFloat().coerceIn(0f..1f),
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        visibilityThreshold = 0.0001f
                    ),
                    initialVelocity = 0f
                )
            }
        }
    }
}
