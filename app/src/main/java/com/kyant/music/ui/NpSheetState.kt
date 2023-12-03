package com.kyant.music.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Stable
object NpSheetState {

    private val expandProgress = Animatable(0f)

    val expandProgressValue by derivedStateOf {
        expandProgress.value
    }

    val targetExpandProgress by derivedStateOf {
        expandProgress.targetValue.roundToInt()
    }

    val collapsed by derivedStateOf {
        expandProgress.value == 0f
    }

    val expanded by derivedStateOf {
        expandProgress.value == 1f
    }

    @Stable
    fun draggableState(
        scope: CoroutineScope,
        screenWidth: Float
    ) = DraggableState {
        scope.launch {
            expandProgress.snapTo((expandProgress.value + it / screenWidth).coerceIn(0f..1f))
        }
    }

    suspend fun togglePane() {
        if (targetExpandProgress < 0.5f) {
            expandPane()
        } else {
            collapsePane()
        }
    }

    suspend fun expandPane(initialVelocity: Float = 0f) {
        expandProgress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = 0.0001f
            ),
            initialVelocity = initialVelocity
        )
    }

    suspend fun collapsePane(initialVelocity: Float = 0f) {
        expandProgress.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = 0.0001f
            ),
            initialVelocity = initialVelocity
        )
    }

    suspend fun fling(
        velocity: Float,
        screenWidth: Float
    ) {
        when {
            velocity > 0 -> expandProgress.animateTo(
                targetValue = (expandProgress.value.toInt() + 1f).coerceAtMost(1f),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = if (screenWidth != 0f) velocity / screenWidth else 0f
            )

            velocity < 0 -> expandProgress.animateTo(
                targetValue = expandProgress.value.toInt().toFloat().coerceAtLeast(0f),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = if (screenWidth != 0f) velocity / screenWidth else 0f
            )

            else -> expandProgress.animateTo(
                targetValue = expandProgress.value.roundToInt().toFloat().coerceIn(0f..1f),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = 0f
            )
        }
    }
}
