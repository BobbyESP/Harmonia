package com.kyant.music.ui

import androidx.compose.runtime.Composable
import com.kyant.music.ui.library.MusicLibrary
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.Screen
import com.kyant.ui.navigation.rememberNavigator

open class AppScreen(isHome: Boolean = false) : Screen(isHome) {
    data object MusicLibrary : AppScreen(true)
    data object Settings : AppScreen()

    companion object {
        @Composable
        fun Container() {
            val navigator = rememberNavigator<AppScreen>(homeScreen = MusicLibrary)
            NavigationScreens(navigator = navigator) { screen ->
                when (screen) {
                    MusicLibrary -> MusicLibrary()
                    Settings -> {}
                }
            }
        }
    }
}
