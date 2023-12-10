package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.style.colorToken
import com.kyant.ui.ProvideTextStyle
import com.kyant.ui.Surface
import com.kyant.ui.style.color.ProvideEmphasis
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography

@Composable
fun LibraryItem(
    onClick: () -> Unit,
    selected: () -> Boolean,
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selected()

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = Rounding.Medium.asSmoothRoundedShape(),
        colorSet = if (isSelected) {
            colorScheme.secondaryContainer
        } else {
            colorToken.card
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideEmphasis(
                iconEmphasis = 0.6f,
                content = icon
            )
            ProvideTextStyle(
                value = typography.labelLarge,
                content = label
            )
        }
    }
}
