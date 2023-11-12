package com.kyant.ui.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.CombinedModifier
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

inline fun Modifier.thenIf(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        CombinedModifier(this, block(Modifier))
    } else {
        this
    }
}

inline fun <T> Modifier.thenIfNotNull(
    other: T,
    block: Modifier.(T & Any) -> Modifier
): Modifier {
    return if (other != null) {
        CombinedModifier(this, block(Modifier, other))
    } else {
        this
    }
}

inline fun <T> Modifier.thenIfAndNotNull(
    condition: Boolean,
    other: T,
    block: Modifier.(T & Any) -> Modifier
): Modifier {
    return if (condition && other != null) {
        CombinedModifier(this, block(Modifier, other))
    } else {
        this
    }
}

@Composable
fun Modifier.clickableWithIndication(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit
) = this.clickable(
    interactionSource = interactionSource,
    indication = indication,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick
)

@Composable
fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) = this.clickable(
    interactionSource = interactionSource,
    indication = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick
)
