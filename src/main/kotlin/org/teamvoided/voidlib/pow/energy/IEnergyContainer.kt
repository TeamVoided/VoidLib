package org.teamvoided.voidlib.pow.energy

import com.mojang.datafixers.util.Pair
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import org.teamvoided.voidlib.pow.energy.interaction.EnergyInteractionResult
import org.teamvoided.voidlib.pow.item.IEnergizedItem
import java.math.BigDecimal

interface IEnergyContainer {
    fun unit(): EnergyUnit
    fun stored(): BigDecimal
    fun maxCapacity(): BigDecimal
    fun canReceive(): Boolean
    fun setReceivability(canReceive: Boolean): Boolean
    fun addEnergy(amount: BigDecimal): EnergyInteractionResult
    fun removeEnergy(amount: BigDecimal): EnergyInteractionResult
    fun setEnergy(amount: BigDecimal): EnergyInteractionResult
    fun transferEnergy(other: IEnergyContainer, amount: BigDecimal, operation: Operation)
    fun <T : IEnergizedItem> transferEnergy(other: T, stack: ItemStack, amount: BigDecimal, operation: Operation)
    fun equalizeWith(containers: List<IEnergyContainer>, stacks: List<Pair<IEnergizedItem, ItemStack>>)
    fun writeNBT(compound: NbtCompound, prefix: String)
    fun readNBT(compound: NbtCompound, prefix: String)
    enum class Operation {
        RECEIVE, GIVE
    }
}