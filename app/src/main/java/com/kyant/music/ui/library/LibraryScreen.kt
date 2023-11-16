package com.kyant.music.ui.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.MainScreen
import com.kyant.music.util.hazeBlur
import com.kyant.ui.navigation.LocalNavigator
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.theme.Theme

enum class LibraryScreen {
    Songs,
    Albums,
    Artists,
    Genres,
    FolderView,
    PlayQueue,
    Favorites,
    Playlists;

    companion object {

        class Controller {

            var isMenuOpen by mutableStateOf(false)

            var currentScreen by mutableStateOf(Songs)

            fun changeTo(screen: LibraryScreen) {
                currentScreen = screen
                isMenuOpen = false
            }
        }

        @Composable
        fun Container() {
            val navigator = LocalNavigator.current as Navigator<MainScreen>
            val controller = remember { Controller() }
            val density = LocalDensity.current
            var sortMenuRect by remember { mutableStateOf(Rect.Zero) }

            Box(
                modifier = Modifier
                    .hazeBlur(
                        RoundRect(sortMenuRect, with(density) { 24.dp.toPx() }, with(density) { 24.dp.toPx() }),
                        backgroundColor = Theme.colorScheme.surfaceContainerHigh.color
                    )
            ) {
                with(controller) {
                    when (controller.currentScreen) {
                        Songs -> Songs()
                        else -> {}
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibility(
                    visible = controller.isMenuOpen,
                    modifier = Modifier
                        .onGloballyPositioned { sortMenuRect = it.boundsInParent() }
                        .pointerInput(Unit) { detectTapGestures { controller.isMenuOpen = false } },
                    enter = slideInVertically(spring(0.8f, 100f)) { -it } + expandIn(
                        animationSpec = spring(1f, 150f),
                        expandFrom = Alignment.TopCenter
                    ) { IntSize(it.width / 2, 0) },
                    exit = slideOutVertically(spring(1f, 250f)) { -it } + shrinkOut(
                        animationSpec = spring(1f, 250f),
                        shrinkTowards = Alignment.TopCenter
                    ) { IntSize(it.width / 2, 0) }
                ) {
                    DisposableEffect(Unit) {
                        onDispose {
                            sortMenuRect = Rect.Zero
                        }
                    }
                    controller.Library(navigator = navigator)
                }
            }
        }
    }
}
