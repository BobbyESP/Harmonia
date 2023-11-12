package com.kyant.ui.util

import androidx.compose.runtime.Stable

@Stable
fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}

@Stable
fun lerp(start: Double, stop: Double, fraction: Float): Double {
    return start + (stop - start) * fraction
}
