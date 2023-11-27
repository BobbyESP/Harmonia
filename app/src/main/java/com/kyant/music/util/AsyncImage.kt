package com.kyant.music.util

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import com.bumptech.glide.integration.compose.GlideImage
import com.kyant.ui.style.color.PerceptualColor

@Composable
fun AsyncImage(
    model: Uri?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) = if (!LocalInspectionMode.current) {
    GlideImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
} else {
    GlideImage(
        model = "",
        contentDescription = contentDescription,
        modifier = modifier.background(PerceptualColor.Red),
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}
