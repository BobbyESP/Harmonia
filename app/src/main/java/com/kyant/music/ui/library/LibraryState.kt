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
import com.kyant.music.data.song.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Stable
object LibraryState {

    var listPaneRoute by mutableStateOf(ListPaneRoute.Songs)
        private set

    var selectedSong: Song? by mutableStateOf(null)

    val hasSelectedSong by derivedStateOf {
        selectedSong != null
    }

    private val paneExpandProgress = Animatable(1f)

    val paneExpandProgressValue by derivedStateOf {
        paneExpandProgress.value
    }

    val targetPaneExpandProgress by derivedStateOf {
        paneExpandProgress.targetValue.roundToInt()
    }

    @Stable
    fun draggableState(
        scope: CoroutineScope,
        screenWidth: Float
    ) = DraggableState {
        scope.launch {
            paneExpandProgress.snapTo((paneExpandProgress.value + it / screenWidth).coerceIn(0f..1f))
        }
    }

    suspend fun navigateTo(route: ListPaneRoute) {
        listPaneRoute = route
        expandPane()
    }

    suspend fun togglePane() {
        if (targetPaneExpandProgress < 0.5f) {
            expandPane()
        } else {
            collapsePane()
        }
    }

    private suspend fun expandPane(initialVelocity: Float = 0f) {
        paneExpandProgress.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = 0.0001f
            ),
            initialVelocity = initialVelocity
        )
    }

    private suspend fun collapsePane(initialVelocity: Float = 0f) {
        paneExpandProgress.animateTo(
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
            velocity > 0 -> paneExpandProgress.animateTo(
                targetValue = (paneExpandProgress.value.toInt() + 1f).coerceAtMost(1f),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = if (screenWidth != 0f) velocity / screenWidth else 0f
            )

            velocity < 0 -> paneExpandProgress.animateTo(
                targetValue = paneExpandProgress.value.toInt().toFloat().coerceAtLeast(0f),
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    visibilityThreshold = 0.0001f
                ),
                initialVelocity = if (screenWidth != 0f) velocity / screenWidth else 0f
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
