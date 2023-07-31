package org.teamvoided.voidlib.vui.v2.animation

import net.minecraft.util.math.MathHelper

class Animation<T>(
    private val duration: Int,
    private var direction: org.teamvoided.voidlib.vui.v2.animation.Animation.Direction,
    private var looping: Boolean,
    private val property: org.teamvoided.voidlib.vui.v2.animation.InterpolatedProperty<T>,
    private val easing: org.teamvoided.voidlib.vui.v2.animation.EasingFunction,
    private val destination: T,
) {
    private var delta = 0f

    fun update(delta: Float) {
        if (this.delta == this.direction.target) {
            if (looping) reverse()
            else return
        }

        this.delta = MathHelper.clamp(this.delta + (delta / duration) * direction.multiplier, 0f, 1f)
        property.interpolate(destination, this.delta, easing)
    }

    fun forwards(): org.teamvoided.voidlib.vui.v2.animation.Animation<T> {
        this.direction = org.teamvoided.voidlib.vui.v2.animation.Animation.Direction.Forwards
        return this
    }

    fun backwards(): org.teamvoided.voidlib.vui.v2.animation.Animation<T> {
        this.direction = org.teamvoided.voidlib.vui.v2.animation.Animation.Direction.Backwards
        return this
    }

    fun reverse(): org.teamvoided.voidlib.vui.v2.animation.Animation<T> {
        this.direction = direction.reverse()
        return this
    }

    fun loop(looping: Boolean): org.teamvoided.voidlib.vui.v2.animation.Animation<T> {
        this.looping = looping
        return this
    }

    fun property() = property
    fun looping() = looping
    fun direction() = direction
    fun isComplete() = !looping && delta == direction.target

    enum class Direction(val multiplier: Float, val target: Float) {
        Forwards(1f, 1f),
        Backwards(-1f, 0f);

        fun reverse(): org.teamvoided.voidlib.vui.v2.animation.Animation.Direction {
            return if (this == org.teamvoided.voidlib.vui.v2.animation.Animation.Direction.Forwards) org.teamvoided.voidlib.vui.v2.animation.Animation.Direction.Backwards else org.teamvoided.voidlib.vui.v2.animation.Animation.Direction.Forwards
        }
    }
}