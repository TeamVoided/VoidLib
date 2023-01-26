package org.team.voided.voidlib.pow.energy

import com.mojang.datafixers.util.Pair
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import org.team.voided.voidlib.pow.NbtHelper
import org.team.voided.voidlib.pow.energy.interaction.EnergyInteractionResult
import org.team.voided.voidlib.pow.item.IEnergizedItem
import java.math.BigDecimal

class EnergyContainer(private val unit: EnergyUnit, private val maxCapacity: BigDecimal) : IEnergyContainer {
    private var stored: BigDecimal = BigDecimal("0")
    private var canReceive = false

    override fun unit(): EnergyUnit {
        return unit
    }

    override fun stored(): BigDecimal {
        return stored
    }

    override fun maxCapacity(): BigDecimal {
        return maxCapacity
    }

    override fun canReceive(): Boolean {
        return canReceive
    }

    override fun setReceivability(canReceive: Boolean): Boolean {
        this.canReceive = canReceive
        return this.canReceive
    }

    override fun addEnergy(amount: BigDecimal): EnergyInteractionResult {
        if (!canReceive()) return EnergyInteractionResult(unit, stored, stored, false)
        val originalAmount = stored
        stored += amount
        return EnergyInteractionResult(unit, originalAmount, stored, true)
    }

    override fun removeEnergy(amount: BigDecimal): EnergyInteractionResult {
        if (!canReceive()) return EnergyInteractionResult(unit, stored, stored, false)
        val originalAmount = stored
        stored -= amount
        return EnergyInteractionResult(unit, originalAmount, stored, true)
    }

    override fun setEnergy(amount: BigDecimal): EnergyInteractionResult {
        val originalAmount = stored
        stored = amount
        return EnergyInteractionResult(unit, originalAmount, stored, true)
    }

    override fun transferEnergy(other: IEnergyContainer, amount: BigDecimal, operation: IEnergyContainer.Operation) {
        if (operation == IEnergyContainer.Operation.RECEIVE) {
            addEnergy(other.unit().convertTo(unit, amount))
            other.removeEnergy(unit.convertTo(other.unit(), amount))
        } else {
            removeEnergy(other.unit().convertTo(unit, amount))
            other.addEnergy(unit.convertTo(other.unit(), amount))
        }
    }

    override fun <T : IEnergizedItem> transferEnergy(
        other: T,
        stack: ItemStack,
        amount: BigDecimal,
        operation: IEnergyContainer.Operation
    ) {
        if (operation == IEnergyContainer.Operation.RECEIVE) {
            addEnergy(other.unit().convertTo(unit, amount))
            other.removeEnergy(stack, unit.convertTo(other.unit(), amount))
        } else {
            removeEnergy(other.unit().convertTo(unit, amount))
            other.addEnergy(stack, unit.convertTo(other.unit(), amount))
        }
    }

    @Suppress("Duplicates")
    override fun equalizeWith(containers: List<IEnergyContainer>, stacks: List<Pair<IEnergizedItem, ItemStack>>) {
        var total: BigDecimal = EnergyUnits.RAW_ENERGY.convertFrom(unit(), stored())
        for (container in containers) {
            total += EnergyUnits.RAW_ENERGY.convertFrom(container.unit(), container.stored())
        }
        for (item in stacks) {
            total += EnergyUnits.RAW_ENERGY.convertFrom(item.first.unit(), item.first.stored(item.second))
        }
        val set: BigDecimal = total / (containers.size + stacks.size).toBigDecimal()
        for (container in containers) {
            container.setEnergy(container.unit().convertFrom(EnergyUnits.RAW_ENERGY, set))
        }
        for (item in stacks) {
            item.first.setEnergy(item.second, item.first.unit().convertFrom(EnergyUnits.RAW_ENERGY, set))
        }
    }

    override fun writeNBT(compound: NbtCompound, prefix: String) {
        NbtHelper.writeToCompound(compound, prefix + "_stored_energy", stored)
        compound.putBoolean(prefix + "_can_receive", canReceive)
    }

    override fun readNBT(compound: NbtCompound, prefix: String) {
        stored = NbtHelper.readFromCompound(compound, prefix + "_stored_energy")
        canReceive = compound.getBoolean(prefix + "_can_receive")
    }
}