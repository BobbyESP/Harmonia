package com.kyant.music.ui

import androidx.compose.runtime.Composable
import com.kyant.music.ui.library.Library
import com.kyant.music.ui.settings.Settings
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.rememberNavigator

@Composable
fun Main() {
    val navigator = rememberNavigator<MainScreen>(homeScreen = MainScreen.Library)
    NavigationScreens(navigator = navigator) { screen ->
        when (screen) {
            MainScreen.Library -> Library()
            MainScreen.Settings -> Settings()
        }
    }
}
