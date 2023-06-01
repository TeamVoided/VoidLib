package org.teamvoided.voidlib.vui.v2.animation

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.voidlib.id
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

fun interface EasingFunction {
    companion object Functions {
        val registry: Registry<EasingFunction> = FabricRegistryBuilder.createSimple<EasingFunction>(RegistryKey.ofRegistry(id("easing_fx_reg"))).buildAndRegister()

        val none = EasingFunction { _ -> 1f }
        val linear = EasingFunction { x -> x }
        val sine = EasingFunction { x -> sin(x * PI - PI / 2).toFloat() * 0.5f + 0.5f }
        val quadratic = EasingFunction { x -> if (x < 0.5) 2 * x.pow(2) else 1 - ((-2 * x + 2).pow(2) / 2) }
        val cubic = EasingFunction { x -> if (x < 0.5) 4 * x.pow(3) else 1 - ((-2 * x + 2).pow(3) / 2) }
        val quartic = EasingFunction { x -> if (x < 0.5) 8 * x.pow(4) else 1 - ((-2 * x + 2).pow(4) / 2) }
        val exponential = EasingFunction { x -> if (x == 0f) 0f else if (x == 1f) 1f else if (x < 0.5) (2f).pow(20 * x - 10) / 2 else (2 - (2f).pow(-20 * x + 10)) / 2 }

        fun init() {
            Registry.register(registry, id("none_efx"), none)
            Registry.register(registry, id("linear_efx"), linear)
            Registry.register(registry, id("sine_efx"), sine)
            Registry.register(registry, id("quadratic_efx"), quadratic)
            Registry.register(registry, id("cubic_efx"), cubic)
            Registry.register(registry, id("quartic_efx"), quartic)
            Registry.register(registry, id("exponential_efx"), exponential)
        }
    }

    operator fun invoke(x: Float): Float
}