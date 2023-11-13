package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.MainScreen
import com.kyant.ui.Button
import com.kyant.ui.Text
import com.kyant.ui.animation.smoothVerticalScroll
import com.kyant.ui.navigation.LocalNavigator
import com.kyant.ui.navigation.Navigator
import com.kyant.ui.theme.Theme

@Composable
fun Library() {
    val navigator = LocalNavigator.current as Navigator<MainScreen>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .smoothVerticalScroll(rememberScrollState())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Library",
            modifier = Modifier.padding(24.dp, 24.dp, 16.dp, 8.dp),
            style = Theme.typography.titleLarge
        )
        Button(onClick = { navigator.push(MainScreen.Settings) }) {
            Text(text = "Settings")
        }
    }
}
