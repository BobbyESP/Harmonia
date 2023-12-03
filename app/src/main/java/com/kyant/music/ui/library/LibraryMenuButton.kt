package com.kyant.music.ui.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import kotlinx.coroutines.launch

@Composable
fun LibraryNavigator.LibraryMenuButton(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    IconButton(
        onClick = {
            scope.launch {
                togglePane()
            }
        },
        modifier = modifier
    ) {
        if (targetPaneExpandProgress < 0.5f) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close menu"
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Default.MenuOpen,
                contentDescription = "Open menu"
            )
        }
    }
}
