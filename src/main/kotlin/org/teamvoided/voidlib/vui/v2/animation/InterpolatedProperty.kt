package org.teamvoided.voidlib.vui.v2.animation

data class InterpolatedProperty<T>(private var property: T, private val interpolator: Interpolator<T>) {
    fun interpolate(next: T, delta: Float, easing: EasingFunction) {
        property = interpolator(property, next, delta, easing)
    }

    fun property() = property
    fun interpolator() = interpolator
}