package com.kyant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.ui.ripple.rememberRipple
import com.kyant.ui.style.color.ColorSet
import com.kyant.ui.style.color.LocalColorSet

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorSet: ColorSet = LocalColorSet.current.copy(color = Color.Transparent),
    shape: Shape = CircleShape,
    size: Dp = 40.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(colorSet.color)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = size / 2
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val parentColorSet = colorSet.copy(color = LocalColorSet.current.color)
        CompositionLocalProvider(value = LocalColorSet provides parentColorSet, content = content)
    }
}
