/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kyant.ui

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import com.kyant.ui.style.color.ColorSet
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.colorScheme
import com.kyant.ui.util.thenIf
import com.kyant.ui.util.thenIfNotNull

@Composable
@NonRestartableComposable
fun RootBackground(
    modifier: Modifier = Modifier,
    colorSet: ColorSet = if (colorScheme.theme.isDark) {
        ColorSet(Color.Black, Color.White)
    } else {
        colorScheme.surfaceContainerLow
    },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorSet provides colorSet) {
        val view = LocalView.current
        val activity = view.context as? Activity
        val background = colorSet.color
        val isDark = colorScheme.theme.isDark

        LaunchedEffect(background) {
            activity?.window?.decorView?.setBackgroundColor(background.toArgb())
        }

        LaunchedEffect(isDark) {
            activity?.window?.let {
                WindowInsetsControllerCompat(it, view).apply {
                    isAppearanceLightStatusBars = !isDark
                    isAppearanceLightNavigationBars = !isDark
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .thenIf(activity == null) {
                    background(background)
                }
                .semantics(mergeDescendants = false) {
                    isTraversalGroup = true
                }
                .pointerInput(Unit) {}
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    colorSet: ColorSet = colorScheme.surface,
    elevation: Dp = 0.dp,
    shadowAlpha: Float = 0.1f,
    border: BorderStroke? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorSet provides colorSet) {
        Box(
            modifier = modifier
                .surface(
                    shape = shape,
                    backgroundColor = colorSet.color,
                    border = border,
                    elevation = elevation,
                    shadowAlpha = shadowAlpha
                )
                .semantics(mergeDescendants = false) {
                    isTraversalGroup = true
                }
                .pointerInput(Unit) {},
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    colorSet: ColorSet = colorScheme.surface,
    elevation: Dp = 0.dp,
    shadowAlpha: Float = 0.1f,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorSet provides colorSet) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = colorSet.color,
                    border = border,
                    elevation = elevation,
                    shadowAlpha = shadowAlpha
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = indication,
                    enabled = enabled,
                    onClick = onClick
                ),
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun Surface(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    colorSet: ColorSet = colorScheme.surface,
    elevation: Dp = 0.dp,
    shadowAlpha: Float = 0.1f,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorSet provides colorSet) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = colorSet.color,
                    border = border,
                    elevation = elevation,
                    shadowAlpha = shadowAlpha
                )
                .selectable(
                    selected = selected,
                    interactionSource = interactionSource,
                    indication = indication,
                    enabled = enabled,
                    onClick = onClick
                ),
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun Surface(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    colorSet: ColorSet = colorScheme.surface,
    elevation: Dp = 0.dp,
    shadowAlpha: Float = 0.1f,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = ripple(),
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalColorSet provides colorSet) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = colorSet.color,
                    border = border,
                    elevation = elevation,
                    shadowAlpha = shadowAlpha
                )
                .toggleable(
                    value = checked,
                    interactionSource = interactionSource,
                    indication = indication,
                    enabled = enabled,
                    onValueChange = onCheckedChange
                ),
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints
        ) {
            content()
        }
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    elevation: Dp,
    shadowAlpha: Float
) = this
    .softShadow(elevation, shape, shadowAlpha, false)
    .thenIfNotNull(border) { border(it, shape) }
    .background(color = backgroundColor, shape = shape)
    .clip(shape)
