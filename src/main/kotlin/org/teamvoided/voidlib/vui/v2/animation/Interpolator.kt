package org.teamvoided.voidlib.vui.v2.animation

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.MathHelper
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.id
import kotlin.math.floor

fun interface Interpolator<T> {
    companion object NumericalInterpolation {
        val interpolators: Registry<Interpolator<*>> = FabricRegistryBuilder.createSimple<Interpolator<*>>(RegistryKey.ofRegistry(id("interpolator_reg"))).buildAndRegister()

        val byteInterpolator = Interpolator<Byte> { start, end, delta, easing -> bLerp(easing(delta), start, end) }
        val shortInterpolator = Interpolator<Short> { start, end, delta, easing -> sLerp(easing(delta), start, end) }
        val intInterpolator = Interpolator<Int> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val longInterpolator = Interpolator<Long> { start, end, delta, easing -> lLerp(easing(delta), start, end) }
        val floatInterpolator = Interpolator<Float> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val doubleInterpolator = Interpolator<Double> { start, end, delta, easing -> MathHelper.lerp(easing(delta).toDouble(), start, end) }

        val colorInterpolator = Interpolator<ARGB> { start, end, delta, easing ->
            ARGB(
                MathHelper.lerp(easing(delta), start.alpha, end.alpha),
                MathHelper.lerp(easing(delta), start.red, end.red),
                MathHelper.lerp(easing(delta), start.green, end.green),
                MathHelper.lerp(easing(delta), start.blue, end.blue)
            )
        }

        fun bLerp(delta: Float, start: Byte, end: Byte): Byte {
            return (start + floor(delta * (end - start)).toInt()).toByte()
        }

        fun sLerp(delta: Float, start: Short, end: Short): Short {
            return (start + floor(delta * (end - start)).toInt()).toShort()
        }

        fun lLerp(delta: Float, start: Long, end: Long): Long {
            return start + floor(delta * (end - start).toDouble()).toLong()
        }

        fun init() {
            Registry.register(interpolators, id("byte"), byteInterpolator)
            Registry.register(interpolators, id("short"), shortInterpolator)
            Registry.register(interpolators, id("int"), intInterpolator)
            Registry.register(interpolators, id("long"), longInterpolator)
            Registry.register(interpolators, id("float"), floatInterpolator)
            Registry.register(interpolators, id("double"), doubleInterpolator)
            Registry.register(interpolators, id("color"), colorInterpolator)
        }
    }

    operator fun invoke(start: T, end: T, delta: Float, easing: EasingFunction): T
}