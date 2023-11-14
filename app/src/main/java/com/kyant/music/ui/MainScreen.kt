package com.kyant.music.ui

import androidx.compose.runtime.Composable
import com.kyant.music.ui.library.LibraryScreen
import com.kyant.music.ui.settings.SettingsScreen
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.Screen
import com.kyant.ui.navigation.rememberNavigator

sealed class MainScreen(isHome: Boolean = false) : Screen(isHome) {
    data object Library : MainScreen(true)
    data object Settings : MainScreen()

    companion object {
        @Composable
        fun Container() {
            val navigator = rememberNavigator<MainScreen>(homeScreen = Library)
            NavigationScreens(navigator = navigator) { screen ->
                when (screen) {
                    Library -> LibraryScreen.Container()
                    Settings -> SettingsScreen.Container()
                }
            }
        }
    }
}
