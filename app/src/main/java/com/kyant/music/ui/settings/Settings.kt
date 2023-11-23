package com.kyant.music.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalScroll
import com.kyant.ui.theme.typography

@Composable
fun Settings() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .smoothVerticalScroll(rememberScrollState())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            modifier = Modifier
                .padding(24.dp, 24.dp, 16.dp, 8.dp),
            style = typography.titleLarge
        )
    }
}
