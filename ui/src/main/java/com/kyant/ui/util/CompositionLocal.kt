package com.kyant.ui.util

internal fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
