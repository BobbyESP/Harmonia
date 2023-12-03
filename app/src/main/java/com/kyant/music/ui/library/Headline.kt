package com.kyant.music.ui.library

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.SingleLineText
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun LibraryState.Headline(
    text: String,
    modifier: Modifier = Modifier
) {
    if (DeviceSpecs.isCompact) {
        SingleLineText(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .graphicsLayer {
                    translationX = (paneExpandProgressValue - targetPaneExpandProgress) * size.width / 2f
                },
            color = colorScheme.primary.color,
            emphasis = lerp(
                0.8f,
                0f,
                (paneExpandProgressValue - targetPaneExpandProgress).absoluteValue * 2f
            ),
            textAlign = TextAlign.Center,
            style = typography.headlineLarge
        )
    } else {
        SingleLineText(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = colorScheme.primary.color,
            emphasis = 0.8f,
            textAlign = TextAlign.Center,
            style = typography.headlineLarge
        )
    }
}
