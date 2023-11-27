package com.kyant.music.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(
    device = "id:pixel_8_pro",
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
)
@Preview(
    device = "id:pixel_8_pro",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    device = "id:pixel",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    locale = "zh-rCN",
    fontScale = 2.0f
)
@Preview(
    device = "spec:parent=6.7in Foldable,orientation=landscape"
)
annotation class MultiPrev
