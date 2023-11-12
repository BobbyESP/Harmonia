package com.kyant.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun OnBackPressed(
    enabled: () -> Boolean,
    onBack: () -> Unit
) {
    BackHandler(
        enabled = remember(enabled()) { enabled() },
        onBack = onBack
    )
}
