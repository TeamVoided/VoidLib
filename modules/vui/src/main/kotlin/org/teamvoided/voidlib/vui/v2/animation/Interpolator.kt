package org.teamvoided.voidlib.vui.v2.animation

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.MathHelper
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.*
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.data.Padding
import kotlin.math.floor

fun interface Interpolator<T> {
    companion object NumericalInterpolation {
        val interpolators: Registry<Interpolator<*>> = FabricRegistryBuilder.createSimple<Interpolator<*>>(RegistryKey.ofRegistry(
            id("interpolator_reg")
        )).buildAndRegister()

        val byteInterpolator = Interpolator<Byte> { start, end, delta, easing -> bLerp(easing(delta), start, end) }
        val shortInterpolator = Interpolator<Short> { start, end, delta, easing -> sLerp(easing(delta), start, end) }
        val intInterpolator = Interpolator<Int> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val longInterpolator = Interpolator<Long> { start, end, delta, easing -> lLerp(easing(delta), start, end) }
        val floatInterpolator = Interpolator<Float> { start, end, delta, easing -> MathHelper.lerp(easing(delta), start, end) }
        val doubleInterpolator = Interpolator<Double> { start, end, delta, easing -> MathHelper.lerp(easing(delta).toDouble(), start, end) }

        val vec2iInterpolator = Interpolator<Vec2i> { start, end, delta, easing ->
            Vec2i(
                MathHelper.lerp(easing(delta), start.x, end.x),
                MathHelper.lerp(easing(delta), start.y, end.y)
            )
        }

        val vec2fInterpolator = Interpolator<Vec2f> { start, end, delta, easing ->
            Vec2f(
                MathHelper.lerp(easing(delta), start.x, end.x),
                MathHelper.lerp(easing(delta), start.y, end.y)
            )
        }

        val vec2dInterpolator = Interpolator<Vec2d> { start, end, delta, easing ->
            Vec2d(
                MathHelper.lerp(easing(delta).d, start.x, end.x),
                MathHelper.lerp(easing(delta).d, start.y, end.y)
            )
        }

        val vec3iInterpolator = Interpolator<Vec3i> { start, end, delta, easing ->
            Vec3i(
                MathHelper.lerp(easing(delta), start.x, end.x),
                MathHelper.lerp(easing(delta), start.y, end.y),
                MathHelper.lerp(easing(delta), start.z, end.z)
            )
        }

        val vec3fInterpolator = Interpolator<Vec3f> { start, end, delta, easing ->
            Vec3f(
                MathHelper.lerp(easing(delta), start.x, end.x),
                MathHelper.lerp(easing(delta), start.y, end.y),
                MathHelper.lerp(easing(delta), start.z, end.z)
            )
        }

        val vec3dInterpolator = Interpolator<Vec3d> { start, end, delta, easing ->
            Vec3d(
                MathHelper.lerp(easing(delta).d, start.x, end.x),
                MathHelper.lerp(easing(delta).d, start.y, end.y),
                MathHelper.lerp(easing(delta).d, start.z, end.z)
            )
        }

        val vec4dInterpolator = Interpolator<Vec4d> { start, end, delta, easing ->
            Vec4d(
                MathHelper.lerp(easing(delta).d, start.x, end.x),
                MathHelper.lerp(easing(delta).d, start.y, end.y),
                MathHelper.lerp(easing(delta).d, start.z, end.z),
                MathHelper.lerp(easing(delta).d, start.w, end.w)
            )
        }

        val colorInterpolator = Interpolator<ARGB> { start, end, delta, easing ->
            ARGB(
                MathHelper.lerp(easing(delta), start.alpha, end.alpha),
                MathHelper.lerp(easing(delta), start.red, end.red),
                MathHelper.lerp(easing(delta), start.green, end.green),
                MathHelper.lerp(easing(delta), start.blue, end.blue)
            )
        }

        val paddingInterpolator = Interpolator<Padding> { start, end, delta, easing ->
            Padding(
                MathHelper.lerp(easing(delta), start.top, end.top),
                MathHelper.lerp(easing(delta), start.bottom, end.bottom),
                MathHelper.lerp(easing(delta), start.left, end.left),
                MathHelper.lerp(easing(delta), start.right, end.right)
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