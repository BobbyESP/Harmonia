/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyant.ui.ripple

import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isUnspecified
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Creates a Ripple node using the values provided.
 *
 * A Ripple is a Material implementation of [Indication] that expresses different [Interaction]s
 * by drawing ripple animations and state layers.
 *
 * A Ripple responds to [PressInteraction.Press] by starting a new [RippleAnimation], and
 * responds to other [Interaction]s by showing a fixed [StateLayer] with varying alpha values
 * depending on the [Interaction].
 *
 * This Ripple node is a low level building block for building IndicationNodeFactory implementations
 * that use a Ripple - higher level design system libraries such as material and material3 provide
 * [Indication] implementations using this node internally. In most cases you should use those
 * factories directly: this node exists for design system libraries to delegate their Ripple
 * implementation to, after querying any required theme values for customizing the Ripple.
 *
 * @param interactionSource the [InteractionSource] used to determine the state of the ripple.
 * @param bounded if true, ripples are clipped by the bounds of the target layout. Unbounded
 * ripples always animate from the target layout center, bounded ripples animate from the touch
 * position.
 * @param radius the radius for the ripple. If [Dp.Unspecified] is provided then the size will be
 * calculated based on the target layout size.
 * @param color the color of the ripple. This color is usually the same color used by the text or
 * iconography in the component. This color will then have [rippleAlpha] applied to calculate the
 * final color used to draw the ripple.
 * @param rippleAlpha the [RippleAlpha] that will be applied to the [color] depending on the state
 * of the ripple.
 */
fun createRippleModifierNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
): DelegatableNode {
    return createPlatformRippleNode(interactionSource, bounded, radius, color, rippleAlpha)
}

/**
 * Android specific Ripple implementation that uses a [RippleDrawable] under the hood, which allows
 * rendering the ripple animation on the render thread (away from the main UI thread). This
 * allows the ripple to animate smoothly even while the UI thread is under heavy load, such as
 * when navigating between complex screens.
 *
 * @see RippleNode
 */
internal fun createPlatformRippleNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
): DelegatableNode {
    return if (IsRunningInPreview) {
        CommonRippleNode(interactionSource, bounded, radius, color, rippleAlpha)
    } else {
        AndroidRippleNode(interactionSource, bounded, radius, color, rippleAlpha)
    }
}

/**
 * Abstract [Modifier.Node] that provides common functionality used by ripple node implementations.
 * Implementing classes should use [stateLayer] to draw the [StateLayer], so they only need to
 * handle showing the ripple effect when pressed, and not other [Interaction]s.
 */
internal abstract class RippleNode(
    private val interactionSource: InteractionSource,
    protected val bounded: Boolean,
    private val radius: Dp,
    private val color: ColorProducer,
    protected val rippleAlpha: () -> RippleAlpha
) : Modifier.Node(), CompositionLocalConsumerModifierNode, DrawModifierNode {
    final override val shouldAutoInvalidate: Boolean = false
    private var stateLayer: StateLayer? = null

    // Calculated inside draw(). This won't happen in Robolectric, so default to 0f to avoid crashes
    var targetRadius: Float = 0f
        private set
    val rippleColor: Color
        get() = color()

    final override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> addRipple(interaction)
                    is PressInteraction.Release -> removeRipple(interaction.press)
                    is PressInteraction.Cancel -> removeRipple(interaction.press)
                    else -> updateStateLayer(interaction, this)
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        targetRadius = if (radius.isUnspecified) {
            // Explicitly calculate the radius instead of using RippleDrawable.RADIUS_AUTO on
            // Android since the latest spec does not match with the existing radius calculation in
            // the framework.
            getRippleEndRadius(bounded, size)
        } else {
            radius.toPx()
        }
        drawContent()
        stateLayer?.run {
            drawStateLayer(targetRadius, rippleColor)
        }
        drawRipples()
    }

    abstract fun DrawScope.drawRipples()
    abstract fun addRipple(interaction: PressInteraction.Press)
    abstract fun removeRipple(interaction: PressInteraction.Press)
    private fun updateStateLayer(interaction: Interaction, scope: CoroutineScope) {
        val stateLayer = stateLayer ?: StateLayer(bounded, rippleAlpha).also { instance ->
            // Invalidate when adding the state layer so we can start drawing it
            invalidateDraw()
            stateLayer = instance
        }
        stateLayer.handleInteraction(interaction, scope)
    }
}

/**
 * Android specific [RippleNode]. This uses a [RippleHostView] provided by [rippleContainer] to
 * draw ripples in the drawing bounds provided within [draw].
 *
 * The state layer is still handled by [stateLayer], and drawn inside Compose.
 */
internal class AndroidRippleNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
) : RippleNode(interactionSource, bounded, radius, color, rippleAlpha), RippleHostKey {
    /**
     * [RippleContainer] attached to the nearest [ViewGroup]. If it hasn't already been
     * created by a another ripple, we will create it and attach it to the hierarchy.
     */
    private var rippleContainer: RippleContainer? = null

    /**
     * Backing [RippleHostView] used to draw ripples for this [RippleIndicationInstance].
     */
    private var rippleHostView: RippleHostView? = null
        set(value) {
            field = value
            invalidateDraw()
        }

    /**
     * Cache the size of the canvas we will draw the ripple into - this is updated each time
     * [draw] is called. This is needed as before we start animating the ripple, we
     * need to know its size (changing the bounds mid-animation will cause us to continue the
     * animation on the UI thread, not the render thread), but the size is only known inside the
     * draw scope.
     */
    private var rippleSize: Size = Size.Zero
    override fun DrawScope.drawRipples() {
        rippleSize = size
        drawIntoCanvas { canvas ->
            rippleHostView?.run {
                // We set these inside addRipple() already, but they may change during the ripple
                // animation, so update them here too.
                // Note that changes to color / alpha will not be reflected in any
                // currently drawn ripples if the ripples are being drawn on the RenderThread,
                // since only the software paint is updated, not the hardware paint used in
                // RippleForeground.
                updateRippleProperties(
                    size = size,
                    radius = targetRadius.roundToInt(),
                    color = rippleColor,
                    alpha = rippleAlpha().pressedAlpha
                )
                draw(canvas.nativeCanvas)
            }
        }
    }

    override fun addRipple(interaction: PressInteraction.Press) {
        rippleHostView = with(getOrCreateRippleContainer()) {
            getRippleHostView().apply {
                addRipple(
                    interaction = interaction,
                    bounded = bounded,
                    size = rippleSize,
                    radius = targetRadius.roundToInt(),
                    color = rippleColor,
                    alpha = rippleAlpha().pressedAlpha,
                    onInvalidateRipple = { invalidateDraw() }
                )
            }
        }
    }

    override fun removeRipple(interaction: PressInteraction.Press) {
        rippleHostView?.removeRipple()
    }

    override fun onDetach() {
        rippleContainer?.run {
            disposeRippleIfNeeded()
        }
    }

    override fun onResetRippleHostView() {
        rippleHostView = null
    }

    private fun getOrCreateRippleContainer(): RippleContainer {
        if (rippleContainer != null) return rippleContainer!!
        val view = findNearestViewGroup(currentValueOf(LocalView))
        rippleContainer = createAndAttachRippleContainerIfNeeded(view)
        return rippleContainer!!
    }
}

private fun createAndAttachRippleContainerIfNeeded(view: ViewGroup): RippleContainer {
    // Try to find existing RippleContainer in the view hierarchy
    for (index in 0 until view.childCount) {
        val child = view.getChildAt(index)
        if (child is RippleContainer) {
            return child
        }
    }
    // Create a new RippleContainer if needed and add to the hierarchy
    return RippleContainer(view.context).apply {
        view.addView(this)
    }
}

/**
 * Returns [initialView] if it is a [ViewGroup], otherwise the nearest parent [ViewGroup] that
 * we will add a [RippleContainer] to.
 *
 * In all normal scenarios this should just be [LocalView], but since [LocalView] is public
 * API theoretically its value can be overridden with a non-[ViewGroup], so we walk up the
 * tree to be safe.
 */
private fun findNearestViewGroup(initialView: View): ViewGroup {
    var view: View = initialView
    while (view !is ViewGroup) {
        val parent = view.parent
        // We should never get to a ViewParent that isn't a View, without finding a ViewGroup
        // first - throw an exception if we do.
        require(parent is View) {
            "Couldn't find a valid parent for $view. Are you overriding LocalView and " +
                "providing a View that is not attached to the view hierarchy?"
        }
        view = parent
    }
    return view
}

/**
 * Whether we are running in a preview or not, to control using the native vs the common ripple
 * implementation. We check this way instead of using [View.isInEditMode] or LocalInspectionMode so
 * this can be called from outside composition.
 */
// TODO(b/188112048): Remove in the future when more versions of Studio support previewing native
//  ripples
private const val IsRunningInPreview = true /*android.os.Build.DEVICE == "layoutlib"*/

/**
 * Represents the layer underneath the press ripple, that displays an overlay for states such as
 * [DragInteraction.Start].
 *
 * Typically, there should be both an 'incoming' and an 'outgoing' layer, so that when
 * transitioning between two states, the incoming of the new state, and the outgoing of the old
 * state can be displayed. However, because:
 *
 * a) the duration of these outgoing transitions are so short (mostly 15ms, which is less than 1
 * frame at 60fps), and hence are barely noticeable if they happen at the same time as an
 * incoming transition
 * b) two layers cause a lot of extra work, and related performance concerns
 *
 * We skip managing two layers, and instead only show one layer. The details for the
 * [AnimationSpec]s used are as follows:
 *
 * No state -> a state = incoming transition for the new state
 * A state -> a different state = incoming transition for the new state
 * A state -> no state = outgoing transition for the old state
 *
 * @see incomingStateLayerAnimationSpecFor
 * @see outgoingStateLayerAnimationSpecFor
 */
private class StateLayer(
    private val bounded: Boolean,
    private val rippleAlpha: () -> RippleAlpha
) {
    private val animatedAlpha = Animatable(0f)
    private val interactions: MutableList<Interaction> = mutableListOf()
    private var currentInteraction: Interaction? = null
    internal fun handleInteraction(interaction: Interaction, scope: CoroutineScope) {
        when (interaction) {
            is HoverInteraction.Enter -> {
                interactions.add(interaction)
            }

            is HoverInteraction.Exit -> {
                interactions.remove(interaction.enter)
            }

            is FocusInteraction.Focus -> {
                interactions.add(interaction)
            }

            is FocusInteraction.Unfocus -> {
                interactions.remove(interaction.focus)
            }

            is DragInteraction.Start -> {
                interactions.add(interaction)
            }

            is DragInteraction.Stop -> {
                interactions.remove(interaction.start)
            }

            is DragInteraction.Cancel -> {
                interactions.remove(interaction.start)
            }

            else -> return
        }
        // The most recent interaction is the one we want to show
        val newInteraction = interactions.lastOrNull()
        if (currentInteraction != newInteraction) {
            if (newInteraction != null) {
                val rippleAlpha = rippleAlpha()
                val targetAlpha = when (interaction) {
                    is HoverInteraction.Enter -> rippleAlpha.hoveredAlpha
                    is FocusInteraction.Focus -> rippleAlpha.focusedAlpha
                    is DragInteraction.Start -> rippleAlpha.draggedAlpha
                    else -> 0f
                }
                val incomingAnimationSpec = incomingStateLayerAnimationSpecFor(newInteraction)
                scope.launch {
                    animatedAlpha.animateTo(targetAlpha, incomingAnimationSpec)
                }
            } else {
                val outgoingAnimationSpec = outgoingStateLayerAnimationSpecFor(currentInteraction)
                scope.launch {
                    animatedAlpha.animateTo(0f, outgoingAnimationSpec)
                }
            }
            currentInteraction = newInteraction
        }
    }

    fun DrawScope.drawStateLayer(radius: Float, color: Color) {
        val alpha = animatedAlpha.value
        if (alpha > 0f) {
            val modulatedColor = color.copy(alpha = alpha)
            if (bounded) {
                clipRect {
                    drawCircle(modulatedColor, radius)
                }
            } else {
                drawCircle(modulatedColor, radius)
            }
        }
    }
}

/**
 * @return the [AnimationSpec] used when transitioning to [interaction], either from a previous
 * state, or no state.
 */
private fun incomingStateLayerAnimationSpecFor(interaction: Interaction): AnimationSpec<Float> {
    return when (interaction) {
        is HoverInteraction.Enter -> DefaultTweenSpec
        is FocusInteraction.Focus -> TweenSpec(durationMillis = 45, easing = LinearEasing)
        is DragInteraction.Start -> TweenSpec(durationMillis = 45, easing = LinearEasing)
        else -> DefaultTweenSpec
    }
}

/**
 * @return the [AnimationSpec] used when transitioning away from [interaction], to no state.
 */
private fun outgoingStateLayerAnimationSpecFor(interaction: Interaction?): AnimationSpec<Float> {
    return when (interaction) {
        is HoverInteraction.Enter -> DefaultTweenSpec
        is FocusInteraction.Focus -> DefaultTweenSpec
        is DragInteraction.Start -> TweenSpec(durationMillis = 150, easing = LinearEasing)
        else -> DefaultTweenSpec
    }
}

/**
 * Default / fallback [AnimationSpec].
 */
private val DefaultTweenSpec = TweenSpec<Float>(durationMillis = 15, easing = LinearEasing)
