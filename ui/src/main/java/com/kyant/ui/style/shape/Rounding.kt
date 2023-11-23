package com.kyant.ui.style.shape

import androidx.annotation.IntRange
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.ui.graphics.SmoothRoundedCornerShape

object Rounding {
    val Full = CornersPercent(50, 50, 50, 50)

    val ExtraLargeTop = Corners(28.dp, 28.dp, 0.dp, 0.dp)
    val ExtraLarge = Corners(28.dp, 28.dp, 28.dp, 28.dp)

    val LargeTop = Corners(16.dp, 16.dp, 0.dp, 0.dp)
    val LargeEnd = Corners(0.dp, 16.dp, 16.dp, 0.dp)
    val LargeStart = Corners(16.dp, 0.dp, 0.dp, 16.dp)
    val Large = Corners(16.dp, 16.dp, 16.dp, 16.dp)

    val Medium = Corners(12.dp, 12.dp, 12.dp, 12.dp)

    val Small = Corners(8.dp, 8.dp, 8.dp, 8.dp)

    val ExtraSmallTop = Corners(4.dp, 4.dp, 0.dp, 0.dp)
    val ExtraSmall = Corners(4.dp, 4.dp, 4.dp, 4.dp)

    val None = Corners(0.dp, 0.dp, 0.dp, 0.dp)
}

data class Corners(
    val topStart: Dp = 0.dp,
    val topEnd: Dp = 0.dp,
    val bottomEnd: Dp = 0.dp,
    val bottomStart: Dp = 0.dp
) {
    fun asRoundedShape() = RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)

    fun asSmoothRoundedShape() = SmoothRoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
}

data class CornersPercent(
    @IntRange(from = 0, to = 100)
    val topStartPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    val topEndPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    val bottomEndPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    val bottomStartPercent: Int = 0
) {
    fun asRoundedShape() = RoundedCornerShape(
        topStartPercent,
        topEndPercent,
        bottomEndPercent,
        bottomStartPercent
    )

    fun asSmoothRoundedShape() = SmoothRoundedCornerShape(
        topStartPercent,
        topEndPercent,
        bottomEndPercent,
        bottomStartPercent
    )
}
