package com.kyant.music.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.AppScreen
import com.kyant.music.ui.core.Card
import com.kyant.music.ui.core.CardItem
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Text
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.typography

@Composable
fun SettingsMenu() {
    val navigator = currentNavigator<AppScreen>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 24.dp)
        ) {
            IconButton(
                onClick = { navigator?.pop() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            SingleLineText(
                text = "Settings",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = colorScheme.primary.color,
                emphasis = 0.8f,
                textAlign = TextAlign.Center,
                style = typography.headlineLarge
            )
        }
        Card {
            CardItem(onClick = {}) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Palette)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Theme",
                            style = typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.5f
                    )
                }
            }
            CardItem(onClick = {}) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp, 8.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Palette)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Appearance",
                            style = typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.5f
                    )
                }
            }
        }
    }
}
