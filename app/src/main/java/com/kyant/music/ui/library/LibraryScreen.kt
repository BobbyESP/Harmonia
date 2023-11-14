package com.kyant.music.ui.library

import androidx.compose.runtime.Composable
import com.kyant.music.ui.MainScreen
import com.kyant.ui.navigation.LocalNavigator
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.navigation.Screen
import com.kyant.ui.navigation.rememberNavigator

sealed class LibraryScreen(isHome: Boolean = false) : Screen(isHome) {
    data object Library : LibraryScreen(true)
    data object Songs : LibraryScreen()
    data object Albums : LibraryScreen()
    data object Artists : LibraryScreen()
    data object Genres : LibraryScreen()

    companion object {
        @Composable
        fun Container() {
            val mainNavigator = LocalNavigator.current as Navigator<MainScreen>
            val navigator = rememberNavigator<LibraryScreen>(homeScreen = Library)
            NavigationScreens(navigator = navigator) { screen ->
                when (screen) {
                    Library -> Library(mainNavigator)
                    Songs -> Songs()
                    else -> {}
                }
            }
        }
    }
}
