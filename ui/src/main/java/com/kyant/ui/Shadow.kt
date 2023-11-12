package com.kyant.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.softShadow(
    elevation: Dp,
    shape: Shape = RectangleShape,
    alpha: Float = 0.1f,
    clip: Boolean = elevation > 0.dp
) = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = clip,
    ambientColor = Color.Black.copy(alpha = alpha),
    spotColor = Color.Black.copy(alpha = alpha)
)
