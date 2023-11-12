package com.kyant.ui.graphics

import androidx.annotation.IntRange
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class SmoothRoundedCornerShape(
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomEnd: CornerSize,
    bottomStart: CornerSize
) : CornerBasedShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomEnd = bottomEnd,
    bottomStart = bottomStart
) {
    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ) = if (topStart + topEnd + bottomEnd + bottomStart == 0.0f) {
        Outline.Rectangle(size.toRect())
    } else {
        Outline.Generic(
            Path().apply {
                val rect = size.toRect()
                moveTo(rect.left + topStart, rect.top)
                lineTo(rect.right - topEnd, rect.top)
                quadraticBezierTo(
                    rect.right,
                    rect.top,
                    rect.right,
                    rect.top + topEnd
                )
                lineTo(rect.right, rect.bottom - bottomEnd)
                quadraticBezierTo(
                    rect.right,
                    rect.bottom,
                    rect.right - bottomEnd,
                    rect.bottom
                )
                lineTo(rect.left + bottomStart, rect.bottom)
                quadraticBezierTo(
                    rect.left,
                    rect.bottom,
                    rect.left,
                    rect.bottom - bottomStart
                )
                lineTo(rect.left, rect.top + topStart)
                quadraticBezierTo(
                    rect.left,
                    rect.top,
                    rect.left + topStart,
                    rect.top
                )
            }
        )
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ) = SmoothRoundedCornerShape(
        topStart = topStart,
        topEnd = topEnd,
        bottomEnd = bottomEnd,
        bottomStart = bottomStart
    )

    override fun toString(): String {
        return "SmoothRoundedCornerShape(topStart = $topStart, topEnd = $topEnd, bottomEnd = " +
            "$bottomEnd, bottomStart = $bottomStart)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SmoothRoundedCornerShape) return false

        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomEnd != other.bottomEnd) return false
        if (bottomStart != other.bottomStart) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        return result
    }
}

fun SmoothRoundedCornerShape(corner: CornerSize) =
    SmoothRoundedCornerShape(corner, corner, corner, corner)

fun SmoothRoundedCornerShape(size: Dp) = SmoothRoundedCornerShape(CornerSize(size))

fun SmoothRoundedCornerShape(size: Float) = SmoothRoundedCornerShape(CornerSize(size))

fun SmoothRoundedCornerShape(percent: Int) = SmoothRoundedCornerShape(CornerSize(percent))

fun SmoothRoundedCornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp
) = SmoothRoundedCornerShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart)
)

fun SmoothRoundedCornerShape(
    topStart: Float = 0.0f,
    topEnd: Float = 0.0f,
    bottomEnd: Float = 0.0f,
    bottomStart: Float = 0.0f
) = SmoothRoundedCornerShape(
    topStart = CornerSize(topStart),
    topEnd = CornerSize(topEnd),
    bottomEnd = CornerSize(bottomEnd),
    bottomStart = CornerSize(bottomStart)
)

fun SmoothRoundedCornerShape(
    @IntRange(from = 0, to = 100)
    topStartPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    topEndPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    bottomEndPercent: Int = 0,
    @IntRange(from = 0, to = 100)
    bottomStartPercent: Int = 0
) = SmoothRoundedCornerShape(
    topStart = CornerSize(topStartPercent),
    topEnd = CornerSize(topEndPercent),
    bottomEnd = CornerSize(bottomEndPercent),
    bottomStart = CornerSize(bottomStartPercent)
)
