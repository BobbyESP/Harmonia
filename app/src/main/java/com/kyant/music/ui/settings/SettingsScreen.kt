package com.kyant.music.ui.settings

import androidx.compose.runtime.Composable
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.Screen
import com.kyant.ui.navigation.rememberNavigator

sealed class SettingsScreen(isHome: Boolean = false) : Screen(isHome) {
    data object Settings : SettingsScreen(true)

    companion object {
        @Composable
        fun Container() {
            val navigator = rememberNavigator<SettingsScreen>(homeScreen = Settings)
            NavigationScreens(navigator = navigator) { screen ->
                when (screen) {
                    Settings -> Settings()
                }
            }
        }
    }
}
