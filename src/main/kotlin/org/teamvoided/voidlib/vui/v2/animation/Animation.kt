package org.teamvoided.voidlib.vui.v2.animation

import net.minecraft.util.math.MathHelper

class Animation<T>(
    private val duration: Int,
    private var direction: Direction,
    private var looping: Boolean,
    private val property: InterpolatedProperty<T>,
    private val easing: EasingFunction,
    private val destination: T,
) {
    private var delta = 0f

    companion object ListExtensions {
        fun <T: List<Animation<A>>, A> T.forwards() {
            forEach { it.forwards() }
        }

        fun <T: List<Animation<*>>> T.forwards() {
            forEach { it.forwards() }
        }

        fun <T: List<Animation<A>>, A> T.backwards() {
            forEach { it.backwards() }
        }

        fun <T: List<Animation<*>>> T.backwards() {
            forEach { it.backwards() }
        }

        fun <T: List<Animation<A>>, A> T.reverse() {
            forEach { it.reverse() }
        }

        fun <T: List<Animation<*>>> T.reverse() {
            forEach { it.reverse() }
        }

        fun <T: List<Animation<A>>, A> T.loop(looping: Boolean) {
            forEach { it.loop(looping) }
        }

        fun <T: List<Animation<*>>> T.loop(looping: Boolean) {
            forEach { it.loop(looping) }
        }
    }

    fun update(delta: Float) {
        if (this.delta == this.direction.target) {
            if (looping) reverse()
            else return
        }

        this.delta = MathHelper.clamp(this.delta + (delta * 50 / duration) * direction.multiplier, 0f, 1f)
        property.interpolate(destination, this.delta, easing)
    }

    fun forwards(): Animation<T> {
        this.direction = Direction.Forwards
        return this
    }

    fun backwards(): Animation<T> {
        this.direction = Direction.Backwards
        return this
    }

    fun reverse(): Animation<T> {
        this.direction = direction.reverse()
        return this
    }

    fun loop(looping: Boolean): Animation<T> {
        this.looping = looping
        return this
    }

    fun looping() = looping
    fun direction() = direction
    fun isComplete() = !looping && delta == direction.target

    enum class Direction(val multiplier: Float, val target: Float) {
        Forwards(1f, 1f),
        Backwards(-1f, 0f);

        fun reverse(): Direction {
            return if (this == Forwards) Backwards else Forwards
        }
    }
}