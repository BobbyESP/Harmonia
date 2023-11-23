package com.kyant.ui.navigation

import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.kyant.ui.theme.color.LocalColorSet
import com.kyant.ui.util.thenIf
import kotlinx.coroutines.launch

@Composable
inline fun <reified S : Screen> ScreenContainer(
    navigator: Navigator<S>,
    screen: S,
    content: @Composable () -> Unit
) {
    val animationSpec = spring<Float>(1f, 200f)
    LaunchedEffect(screen.isActive) {
        if (screen.isActive) {
            screen.fraction.animateTo(1f, animationSpec)
        } else {
            screen.fraction.animateTo(0f, animationSpec)
            navigator.screens.removeLastOrNull()
        }
    }

    val color = LocalColorSet.current.color
    val width = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val scope = rememberCoroutineScope()

    val activeScreen by remember(navigator.screens.toList()) {
        derivedStateOf {
            val routes = navigator.screens.take(navigator.currentRouteDepth + 2)
            val index = routes.indexOf(screen)
            if (index == -1) {
                null
            } else {
                routes.getOrNull(index + 1)
            }
        }
    }

    /*val previousScreen by remember(screens.toList()) {
        derivedStateOf {
            val index = screens.indexOf(screen)
            if (index == -1) {
                null
            } else {
                screens.getOrNull(index - 1)
            }
        }
    }*/

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (screen.fraction.value != 0f && screen.fraction.value != 1f) {
                    drawRect(
                        Color.Black,
                        alpha = (0.4f * screen.fraction.value).coerceIn(0f..1f)
                    )
                }
            }
            .graphicsLayer {
                val width = if (isLtr) size.width else -size.width
                var offset = width * (1f - screen.fraction.value)
                if (screen.fraction.value == 0f) {
                    compositingStrategy = CompositingStrategy.ModulateAlpha
                    this.alpha = 0f
                    return@graphicsLayer
                }
                if (screen.fraction.value == 1f || screen.fraction.isRunning) {
                    val screen = activeScreen
                    if (screen != null) {
                        if (screen.fraction.value == 1f) {
                            compositingStrategy = CompositingStrategy.ModulateAlpha
                            this.alpha = 0f
                            return@graphicsLayer
                        } else {
                            offset = -width / 4f * screen.fraction.value
                        }
                    }
                }
                translationX = offset
            }
            .drawBehind {
                if (screen.fraction.value != 0f && screen.fraction.value != 1f) {
                    drawRect(color)
                }
            }
            .graphicsLayer {
                if (screen.fraction.value == 1f || screen.fraction.isRunning) {
                    val screen = activeScreen
                    if (screen != null) {
                        transformOrigin = TransformOrigin(0f, 0.5f)
                        val scale = 1f - 0.1f * screen.fraction.value
                        scaleX = scale
                        scaleY = scale
                    }
                }
            }
            .thenIf(!screen.isHome) {
                val state = rememberDraggableState { delta ->
                    if (delta.isNaN()) return@rememberDraggableState
                    scope.launch {
                        screen.fraction.snapTo((screen.fraction.value + delta / width).coerceIn(0f..1f))
                    }
                }

                draggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = isLtr,
                    onDragStarted = { navigator.addRoute(screen) },
                    onDragStopped = { velocity ->
                        if (screen.fraction.value == 1f && velocity > 0) return@draggable
                        scope.launch {
                            val targetPercentage = when {
                                velocity > 0 -> 1f
                                velocity < 0 -> 0f
                                else -> if (screen.fraction.value > 0.5f) 1f else 0f
                            }
                            if (targetPercentage == 0f) {
                                navigator.removeRoute(screen)
                                screen.fraction.animateTo(0f, animationSpec, velocity / width)
                                navigator.screens.remove(screen)
                            } else {
                                navigator.addRoute(screen)
                                screen.fraction.animateTo(1f, animationSpec, velocity / width)
                            }
                        }
                    }
                )
            }
    ) {
        content()
        /*if (screen !is HomeScreen) {
            previousScreen?.let { screen ->
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(value = LocalColorToken provides colorScheme.primary.inverse()) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            size = 16.dp
                        )
                        Text(
                            text = screen.toString(),
                            style = Theme.typography.labelMedium
                        )
                    }
                }
            }
        }*/
    }
}
