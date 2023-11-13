package com.kyant.music.ui

import com.kyant.ui.navigation.Screen

sealed class MainScreen(isHome: Boolean = false) : Screen(isHome) {
    data object Library : MainScreen(true)
    data object Settings : MainScreen()
}
