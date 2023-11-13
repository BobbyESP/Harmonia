package com.kyant.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kyant.ui.animation.FractionAnimatable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class Screen(val isHome: Boolean) : Parcelable {

    @IgnoredOnParcel
    open var isActive by mutableStateOf(isHome)

    @IgnoredOnParcel
    open val fraction = FractionAnimatable(if (isHome) 1f else 0f)

    open fun enter() = if (isHome) {
        Unit
    } else {
        isActive = true
    }

    open fun exit() = if (isHome) {
        Unit
    } else {
        isActive = false
    }
}
