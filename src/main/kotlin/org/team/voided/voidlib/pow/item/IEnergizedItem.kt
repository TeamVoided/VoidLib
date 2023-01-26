package org.team.voided.voidlib.pow.item

import com.mojang.datafixers.util.Pair
import net.minecraft.item.ItemStack
import org.team.voided.voidlib.pow.energy.EnergyUnit
import org.team.voided.voidlib.pow.energy.IEnergyContainer
import org.team.voided.voidlib.pow.energy.interaction.EnergyInteractionResult
import java.math.BigDecimal

interface IEnergizedItem {
    fun setUnit(unit: EnergyUnit)
    fun unit(): EnergyUnit
    fun setMaxCapacity(maxCapacity: BigDecimal)
    fun getMaxCapacity(): BigDecimal
    fun stored(stack: ItemStack): BigDecimal
    fun setEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult
    fun addEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult
    fun removeEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult
    fun <T : IEnergizedItem> transferEnergy(
        self: ItemStack,
        otherClass: T,
        otherStack: ItemStack,
        amount: BigDecimal,
        operation: IEnergyContainer.Operation
    )

    fun transferEnergy(
        self: ItemStack,
        other: IEnergyContainer,
        amount: BigDecimal,
        operation: IEnergyContainer.Operation
    )

    fun equalizeWith(
        self: ItemStack,
        containers: List<IEnergyContainer>,
        stacks: List<Pair<IEnergizedItem, ItemStack>>
    )
}