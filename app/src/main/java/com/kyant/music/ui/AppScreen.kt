package com.kyant.music.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.library.MusicLibrary
import com.kyant.music.ui.theme.DefaultTheme
import com.kyant.music.util.MultiPrev
import com.kyant.ui.RootBackground
import com.kyant.ui.navigation.NavigationScreens
import com.kyant.ui.navigation.Screen
import com.kyant.ui.navigation.rememberNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

@MultiPrev
@Composable
fun AppScreenPreview() {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            mediaStore.scan()
        }
    }
    DefaultTheme {
        RootBackground {
            AppScreen.Container()
        }
    }
}
