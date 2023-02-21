package org.teamvoided.voidlib.pow.energy

import net.minecraft.registry.Registry
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.pow.RGB
import org.teamvoided.voidlib.pow.registry.EnergyRegistries
import java.math.BigDecimal

object EnergyUnits {
    val RAW_ENERGY = EnergyUnit(BigDecimal("100"), id("raw_energy_unit"), RGB(255, 255, 255))
    val REDSTONE_FLUX = EnergyUnit(BigDecimal("1"), id("redstone_flux_unit"), RGB(233, 22, 32))
    val FABRIC_ENERGY = EnergyUnit(BigDecimal("2"), id("fabric_energy_unit"), RGB(185, 52, 203))
    fun register() {
        Registry.register(EnergyRegistries.UNIT, RAW_ENERGY.id(), RAW_ENERGY)
        Registry.register(EnergyRegistries.UNIT, REDSTONE_FLUX.id(), REDSTONE_FLUX)
        Registry.register(EnergyRegistries.UNIT, FABRIC_ENERGY.id(), FABRIC_ENERGY)
    }
}