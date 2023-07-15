package org.teamvoided.voidlib.pow.registry

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.pow.energy.EnergyUnit

object EnergyRegistries {
    val UNIT_REGKEY: RegistryKey<Registry<EnergyUnit>> = RegistryKey.ofRegistry(id("energy_unit_reg"))
    val UNIT: Registry<EnergyUnit> = FabricRegistryBuilder.createSimple(UNIT_REGKEY).buildAndRegister()
}