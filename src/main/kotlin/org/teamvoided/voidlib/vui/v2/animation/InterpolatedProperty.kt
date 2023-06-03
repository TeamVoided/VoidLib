package org.teamvoided.voidlib.vui.v2.animation

data class InterpolatedProperty<T>(private var start: T, private val interpolator: Interpolator<T>, private val setter: (T) -> Unit) {
    private var lastValue: T = start

    fun interpolate(next: T, delta: Float, easing: EasingFunction) {
        lastValue = interpolator(start, next, delta, easing)
        setter(lastValue)
    }

    fun start() = start
    fun property() = lastValue
    fun interpolator() = interpolator
}