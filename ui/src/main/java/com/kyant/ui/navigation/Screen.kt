package com.kyant.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kyant.ui.animation.FractionAnimatable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class HomeScreen : Screen() {

    @IgnoredOnParcel
    final override var isActive by mutableStateOf(true)

    @IgnoredOnParcel
    final override val fraction = FractionAnimatable(1f)

    final override fun enter() = Unit

    final override fun exit() = Unit
}

@Parcelize
open class Screen : Parcelable {

    @IgnoredOnParcel
    open var isActive by mutableStateOf(false)

    @IgnoredOnParcel
    open val fraction = FractionAnimatable(0f)

    open fun enter() {
        isActive = true
    }

    open fun exit() {
        isActive = false
    }
}
