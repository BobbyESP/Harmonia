package com.kyant.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
inline fun <reified S : Screen> NavigationScreens(
    navigator: Navigator<S>,
    modifier: Modifier = Modifier,
    crossinline content: @Composable Navigator<S>.(S) -> Unit
) {
    Box(modifier = modifier) {
        CompositionLocalProvider(LocalNavigator provides navigator) {
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
