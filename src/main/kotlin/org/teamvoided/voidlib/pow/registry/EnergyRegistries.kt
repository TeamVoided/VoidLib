package org.teamvoided.voidlib.pow.registry

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.pow.energy.EnergyUnit

object EnergyRegistries {
    val UNIT: Registry<EnergyUnit> =
        FabricRegistryBuilder.createSimple(EnergyUnit::class.java, id("energy_unit_reg"))
            .buildAndRegister()
}