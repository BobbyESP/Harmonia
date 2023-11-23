package com.kyant.music.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.scale
import androidx.core.graphics.set
import com.kyant.m3color.hct.Hct
import com.kyant.ui.theme.colorScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun <T> AsyncBlurredImage(
    model: T,
    bitmap: (T) -> ImageBitmap?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    sampleCount: Int = 3,
    tone: Double = if (colorScheme.theme.isDark) 50.0 else 90.0
) {
    val blurredBitmap by produceState<ImageBitmap?>(initialValue = null, key1 = model) {
        value = withContext(Dispatchers.IO) {
            bitmap(model)?.asAndroidBitmap()?.scale(sampleCount, sampleCount)?.apply {
                for (x in 0 until sampleCount.coerceAtMost(width)) {
                    for (y in 0 until sampleCount.coerceAtMost(height)) {
                        this[x, y] = Hct.fromInt(getPixel(x, y)).apply {
                            this.tone = tone
                        }.toInt()
                    }
                }
            }?.asImageBitmap()
        }
    }

    blurredBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}
