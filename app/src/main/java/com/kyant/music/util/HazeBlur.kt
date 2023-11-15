package com.kyant.music.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.ui.theme.Theme
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.haze

@Stable
@Composable
fun Modifier.hazeBlur(
    vararg area: Rect,
    backgroundColor: Color = Theme.colorScheme.background.color,
    tint: Color = Theme.colorScheme.background.color.copy(
        alpha = if (Theme.colorScheme.darkTheme) 0.5f else 0.8f
    ),
    blurRadius: Dp = 24.dp,
    noiseFactor: Float = HazeDefaults.noiseFactor
): Modifier = this then haze(
    area = area,
    backgroundColor,
    tint,
    blurRadius,
    noiseFactor
)
