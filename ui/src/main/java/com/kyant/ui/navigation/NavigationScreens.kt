package com.kyant.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun NavigationScreens(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    content: @Composable Navigator.(Screen) -> Unit
) {
    CompositionLocalProvider(LocalNavigator provides navigator) {
        Box(modifier = modifier) {
            navigator.screens.forEach { screen ->
                ScreenContainer(navigator, screen) {
                    content(navigator, screen)
                }
            }
        }
    }

    OnBackPressed(enabled = { navigator.canGoBack }) {
        navigator.hide()
    }
}
