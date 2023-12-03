package com.kyant.music.util

import androidx.compose.material3.adaptive.collectWindowSizeAsState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

object DeviceSpecs {

    inline val size: IntSize
        @Composable
        get() = collectWindowSizeAsState().value

    val isCompact: Boolean
        @Composable
        get() {
            val windowAdaptiveInfo = currentWindowAdaptiveInfo()
            return windowAdaptiveInfo.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
        }

    val isLarge: Boolean
        @Composable
        get() = !isCompact

    val isVerticallyFoldable: Boolean
        @Composable
        get() {
            val windowAdaptiveInfo = currentWindowAdaptiveInfo()
            return remember(windowAdaptiveInfo) {
                windowAdaptiveInfo.windowPosture.separatingVerticalHingeBounds.isNotEmpty()
            }
        }

    val hingeWidth: Dp
        @Composable
        get() = if (isVerticallyFoldable) {
            val windowAdaptiveInfo = currentWindowAdaptiveInfo()
            with(LocalDensity.current) {
                remember(windowAdaptiveInfo) {
                    windowAdaptiveInfo.windowPosture.separatingVerticalHingeBounds.first().width.toDp()
                }
            }
        } else {
            0.dp
        }
}
