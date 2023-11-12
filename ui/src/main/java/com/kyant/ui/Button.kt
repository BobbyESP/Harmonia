package com.kyant.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.ui.theme.ColorToken
import com.kyant.ui.theme.LocalColorToken
import com.kyant.ui.theme.Theme

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorToken: ColorToken = Theme.colorScheme.primary,
    disabledColorToken: ColorToken = ColorToken(
        Theme.colorScheme.surface.contentColor.copy(alpha = 0.12f),
        Theme.colorScheme.surface.contentColor.copy(alpha = 0.38f)
    ),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val buttonColorToken = if (enabled) {
        colorToken
    } else {
        disabledColorToken
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colorToken = buttonColorToken,
        border = border,
        interactionSource = interactionSource
    ) {
        ProvideTextStyle(value = Theme.typography.labelLarge) {
            CompositionLocalProvider(value = LocalIconSize provides 20.dp) {
                Row(
                    modifier = Modifier.padding(16.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun FilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorToken: ColorToken = Theme.colorScheme.primaryContainer,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorToken = colorToken,
    interactionSource = interactionSource,
    content = content
)

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = Theme.colorScheme.primary.color,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorToken = ColorToken(Color.Transparent, contentColor),
    disabledColorToken = ColorToken(
        Color.Transparent,
        Theme.colorScheme.surface.contentColor.copy(alpha = 0.12f)
    ),
    interactionSource = interactionSource,
    content = content
)

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LocalColorToken.current.contentColor,
    border: BorderStroke? = BorderStroke(1.dp, color),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorToken = ColorToken(Color.Transparent, color),
    disabledColorToken = ColorToken(
        Color.Transparent,
        Theme.colorScheme.surface.contentColor.copy(alpha = 0.12f)
    ),
    border = border,
    interactionSource = interactionSource,
    content = content
)
