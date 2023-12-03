package com.kyant.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kyant.ui.navigation.OnBackPressed
import com.kyant.ui.style.motion.Duration
import com.kyant.ui.style.motion.Easing
import com.kyant.ui.style.motion.Easing.with
import com.kyant.ui.style.shape.Rounding

@Composable
fun BottomSheetDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    shape: Shape = Rounding.ExtraLargeTop.asSmoothRoundedShape(),
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .pointerInput(dismissable) {
                    detectTapGestures {
                        if (dismissable) {
                            onDismissRequest()
                        }
                    }
                }
        )

        OnBackPressed(enabled = { visible && dismissable }) {
            onDismissRequest()
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
            .widthIn(max = 640.dp)
            .statusBarsPadding()
            .padding(top = 24.dp)
            .clip(shape),
        enter = slideInVertically(
            spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = IntOffset.VisibilityThreshold
            )
        ) { it / 2 } + expandVertically(
            spring(
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = IntSize.VisibilityThreshold
            )
        ),
        exit = slideOutVertically(Easing.EmphasizedAccelerate with Duration.SHORT_4) { it / 2 } +
            shrinkVertically(Easing.EmphasizedAccelerate with Duration.SHORT_4)
    ) { content() }
}
