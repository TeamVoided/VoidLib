package org.teamvoided.voidlib.vui.v2.animation

import net.minecraft.util.math.MathHelper
import kotlin.math.floor

fun interface Interpolator<T> {
    companion object NumericalInterpolation {
        val byteInterpolator = Interpolator<Byte> { start, end, delta, easing -> bLerp(easing(delta), start, end) }
        val shortInterpolator = Interpolator<Short> { start, end, delta, easing -> sLerp(easing(delta), start, end) }
        val intInterpolator = Interpolator<Int> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val longInterpolator = Interpolator<Long> { start, end, delta, easing -> lLerp(easing(delta), start, end) }
        val floatInterpolator = Interpolator<Float> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val doubleInterpolator = Interpolator<Double> { start, end, delta, easing -> MathHelper.lerp(easing(delta).toDouble(), start, end) }

        fun bLerp(delta: Float, start: Byte, end: Byte): Byte {
            return (start + floor(delta * (end - start)).toInt()).toByte()
        }

        fun sLerp(delta: Float, start: Short, end: Short): Short {
            return (start + floor(delta * (end - start)).toInt()).toShort()
        }

        fun lLerp(delta: Float, start: Long, end: Long): Long {
            return start + floor(delta * (end - start).toDouble()).toLong()
        }
    }

    operator fun invoke(start: T, end: T, delta: Float, easing: EasingFunction): T
}