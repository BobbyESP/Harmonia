package com.kyant.music.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.kyant.music.data.Album
import com.kyant.music.data.song.Song
import com.kyant.music.util.toBitmap
import com.kyant.ui.theme.Theme
import com.kyant.ui.theme.color.ColorScheme
import com.kyant.ui.theme.colorScheme
import com.kyant.ui.util.extractColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DynamicTheme(
    song: Song? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemColorScheme = colorScheme
    val colorTheme = colorScheme.theme
    val colorScheme by produceState(initialValue = systemColorScheme, colorTheme, song) {
        value = withContext(Dispatchers.IO) {
            song?.thumbnailUri?.toBitmap(context)?.extractColors()?.let {
                ColorScheme(colorTheme.copy(it.first()))
            } ?: systemColorScheme
        }
    }

    Theme(colorScheme = colorScheme, content = content)
}

@Composable
fun DynamicTheme(
    album: Album? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemColorScheme = colorScheme
    val colorTheme = colorScheme.theme
    val colorScheme by produceState(initialValue = systemColorScheme, colorTheme, album) {
        value = withContext(Dispatchers.IO) {
            album?.thumbnailUri?.toBitmap(context)?.extractColors()?.let {
                ColorScheme(colorTheme.copy(it.first()))
            } ?: systemColorScheme
        }
    }

    Theme(colorScheme = colorScheme, content = content)
}
