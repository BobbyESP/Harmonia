package com.kyant.ui.style.motion

import com.kyant.ui.animation.CubicBezierEasing

val Emphasized = CubicBezierEasing(0.2f, 0f, 0f, 1f)

val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)

val Standard = CubicBezierEasing(0.2f, 0f, 0f, 1f)

val StandardAccelerate = CubicBezierEasing(0.3f, 0f, 1f, 1f)

val StandardDecelerate = CubicBezierEasing(0f, 0f, 0f, 1f)

val Linear = CubicBezierEasing(0f, 0f, 1f, 1f)
