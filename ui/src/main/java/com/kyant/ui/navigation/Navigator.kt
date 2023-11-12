package com.kyant.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

val LocalNavigator = staticCompositionLocalOf<Navigator?> { null }

@Composable
fun rememberNavigator(homeScreen: HomeScreen): Navigator {
    return rememberSaveable(homeScreen) { Navigator(homeScreen) }
}

@Parcelize
class Navigator(private val homeScreen: HomeScreen) : Parcelable {

    @IgnoredOnParcel
    val screens = mutableStateListOf<Screen>(homeScreen)

    @IgnoredOnParcel
    var currentRouteDepth by mutableIntStateOf(0)

    @IgnoredOnParcel
    val currentScreen by derivedStateOf {
        screens.getOrNull(currentRouteDepth)
    }

    @IgnoredOnParcel
    val canGoBack by derivedStateOf {
        currentRouteDepth != 0
    }

    fun push(screen: Screen) {
        if (screen in screens.take(currentRouteDepth + 1)) {
            screen.enter()
            return
        }
        while (currentRouteDepth < screens.lastIndex) {
            screens.removeLastOrNull()
        }
        screens.add(screen)
        currentRouteDepth++
        screen.enter()
    }

    fun pop() {
        if (currentRouteDepth < 1) return
        val screen = currentScreen
        currentRouteDepth--
        screen?.exit()
    }

    fun removeRoute(screen: Screen) {
        if (currentRouteDepth < 1) return
        currentRouteDepth--
        screen.exit()
    }

    fun addRoute(screen: Screen) {
        if (screen in screens.take(currentRouteDepth + 1)) return
        while (currentRouteDepth < screens.lastIndex) {
            screens.removeLastOrNull()
        }
        screens.add(screen)
        currentRouteDepth++
    }

    fun hide() {
        if (currentRouteDepth < 1) return
        val screen = currentScreen
        currentRouteDepth--
        screen?.exit()
    }
}
