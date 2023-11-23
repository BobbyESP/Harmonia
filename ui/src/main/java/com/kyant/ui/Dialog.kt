package com.kyant.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.ui.style.colorScheme
import com.kyant.ui.util.thenIf

@Composable
fun Dialog(
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    content: @Composable () -> Unit
) {
    BaseDialog(
        isShown = isShown,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        dialogPadding = PaddingValues(24.dp),
        consumeSystemWindowInsets = true,
        dismissable = dismissable,
        content = content
    )
}

@Composable
fun SheetDialog(
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissable: Boolean = true,
    content: @Composable () -> Unit
) {
    BaseDialog(
        isShown = isShown,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        dialogPadding = PaddingValues(),
        consumeSystemWindowInsets = false,
        dismissable = dismissable,
        content = content
    )
}

@Composable
private inline fun BaseDialog(
    isShown: Boolean,
    crossinline onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dialogPadding: PaddingValues,
    consumeSystemWindowInsets: Boolean,
    dismissable: Boolean = true,
    crossinline content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isShown,
        modifier = Modifier
            .fillMaxSize()
            .thenIf(dismissable && isShown) {
                pointerInput(Unit) {
                    detectTapGestures { onDismissRequest() }
                }
            }
            .thenIf(!dismissable && isShown) {
                pointerInput(Unit) {
                    detectTapGestures {}
                }
            }
    ) {
        BackHandler(dismissable && isShown) {
            onDismissRequest()
        }
        BackHandler(!dismissable) {}
        Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.2f)))
    }
    AnimatedVisibility(
        visible = isShown,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.Bottom),
        enter = slideInVertically(
            animationSpec = spring(0.7f, 100f, IntOffset.VisibilityThreshold)
        ) { it } + scaleIn(
            animationSpec = spring(1f, 100f, 0.0001f),
            initialScale = 0.6f
        ),
        exit = slideOutVertically(
            animationSpec = spring(1f, 500f, IntOffset.VisibilityThreshold)
        ) { it }
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .thenIf(consumeSystemWindowInsets) { systemBarsPadding() }
                .padding(dialogPadding)
                .softShadow(8.dp, RoundedCornerShape(24.dp)),
            colorToken = colorScheme.surfaceContainerLowest
        ) {
            content()
        }
    }
}
