package com.kyant.music.ui.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.music.ui.theme.colorToken
import com.kyant.ui.Surface
import com.kyant.ui.style.shape.Rounding

@Composable
inline fun CardItem(
    modifier: Modifier = Modifier,
    crossinline content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        shape = Rounding.ExtraSmall.asSmoothRoundedShape(),
        colorSet = colorToken.card
    ) {
        content()
    }
}
