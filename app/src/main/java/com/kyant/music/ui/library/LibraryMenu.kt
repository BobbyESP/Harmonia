package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.AppScreen
import com.kyant.music.ui.style.colorToken
import com.kyant.ui.FilledTonalButton
import com.kyant.ui.Icon
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.style.color.PerceptualColor
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.monochromeColorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LibraryState.LibraryMenu(modifier: Modifier = Modifier) {
    val navigator = currentNavigator<AppScreen>()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Headline(
            text = "Library",
            modifier = Modifier.padding(top = 24.dp)
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Surface(
                shape = Rounding.Large.asSmoothRoundedShape(),
                colorSet = colorToken.card
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Scan your library",
                        modifier = Modifier.padding(16.dp, 8.dp),
                        style = typography.headlineSmall
                    )
                    FilledTonalButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                mediaStore.scan()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        enabled = !mediaStore.isScanning
                    ) {
                        Text(text = "Scan")
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val scope = rememberCoroutineScope()

                LibraryItem(
                    onClick = { scope.launch { navigate(ListPaneRoute.Songs) } },
                    selected = { listPaneRoute == ListPaneRoute.Songs },
                    label = { Text(text = "Songs") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            tint = PerceptualColor.LightBlue.colorScheme.primary.color
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                LibraryItem(
                    onClick = { scope.launch { navigate(ListPaneRoute.Albums) } },
                    selected = { listPaneRoute == ListPaneRoute.Albums },
                    label = { Text(text = "Albums") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Album,
                            tint = PerceptualColor.Green.colorScheme.primary.color
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                LibraryItem(
                    onClick = { navigator?.push(AppScreen.Settings) },
                    selected = { false },
                    label = { Text(text = "Settings") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            tint = PerceptualColor.Gray.monochromeColorScheme.primary.color
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
