package com.kyant.music.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.AppScreen
import com.kyant.ui.FilledTonalButton
import com.kyant.ui.HorizontalDivider
import com.kyant.ui.Icon
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.navigation.currentNavigator
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.colorScheme
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
                shape = Rounding.Large.asSmoothRoundedShape()
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

            Column {
                val scope = rememberCoroutineScope()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                navigate(ListPaneRoute.Songs)
                            }
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Songs",
                        modifier = Modifier.weight(1f),
                        color = if (listPaneRoute == ListPaneRoute.Songs) {
                            colorScheme.primary.color
                        } else {
                            LocalColorSet.current.onColor
                        },
                        style = typography.titleLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.6f
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(start = 8.dp),
                    thickness = 2.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                navigate(ListPaneRoute.Albums)
                            }
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Albums",
                        modifier = Modifier.weight(1f),
                        color = if (listPaneRoute == ListPaneRoute.Albums) {
                            colorScheme.primary.color
                        } else {
                            LocalColorSet.current.onColor
                        },
                        style = typography.titleLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.6f
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(start = 8.dp),
                    thickness = 2.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigator?.push(AppScreen.Settings) }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Settings",
                        modifier = Modifier.weight(1f),
                        style = typography.titleLarge
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.NavigateNext,
                        emphasis = 0.6f
                    )
                }
            }
        }
    }
}
