package com.kyant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.color.applyEmphasis
import com.kyant.ui.style.colorScheme

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 0.75.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, 0) {
                    placeable.placeRelative(0, -(thickness.toPx() / 2f).toInt(), 1f)
                }
            }
            .height(thickness)
            .background(
                LocalColorSet.current.onColor.applyEmphasis(if (colorScheme.theme.isDark) 0.25f else 0.1f)
            )
    )
}
