package com.kyant.ui.util

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.scale
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.quantize.QuantizerCelebi
import com.kyant.m3color.score.Score

@Stable
inline fun Color.transformInHct(block: Hct.() -> Unit): Color {
    return Hct.fromInt(this.toArgb()).apply(block).toInt().let { Color(it).copy(alpha = alpha) }
}

fun Bitmap.extractColors(
    maxColors: Int = 5,
    size: IntSize = IntSize(48, 48)
): List<Color> {
    val pixels = this.scale(size.width, size.height).asImageBitmap().toPixelMap().buffer
    val quantizerResult = QuantizerCelebi.quantize(pixels, maxColors)
    val colors = Score.score(quantizerResult)
    return colors.map { Color(it) }
}
