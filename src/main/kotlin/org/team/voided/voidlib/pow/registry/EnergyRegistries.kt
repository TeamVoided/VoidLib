package org.team.voided.voidlib.pow.registry

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import org.team.voided.voidlib.id
import org.team.voided.voidlib.pow.energy.EnergyUnit

object EnergyRegistries {
    val UNIT: Registry<EnergyUnit> =
        FabricRegistryBuilder.createSimple(EnergyUnit::class.java, id("energy_unit_reg"))
            .buildAndRegister()
}