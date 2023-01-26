package org.team.voided.voidlib.pow.item

import com.mojang.datafixers.util.Pair
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import org.team.voided.voidlib.pow.NbtHelper
import org.team.voided.voidlib.pow.Pow.Companion.formatDouble
import org.team.voided.voidlib.pow.client.gui.EnergyBarTooltipData
import org.team.voided.voidlib.pow.energy.EnergyUnits
import org.team.voided.voidlib.pow.energy.IEnergyContainer
import org.team.voided.voidlib.pow.energy.interaction.EnergyInteractionResult
import java.math.BigDecimal
import java.util.*

interface StandardEnergizedItem : IEnergizedItem {
    fun appendHoverTextDefault(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext?) {
        if (Screen.hasShiftDown()) {
            tooltip.add(
                Text.translatable("quilt_energy.energyitem.unit", unit().getName())
                    .formatted(Formatting.LIGHT_PURPLE)
            )
            tooltip.add(
                Text.translatable(
                    "quilt_energy.energyitem.max_capacity",
                    formatDouble(getMaxCapacity().toDouble(), 64)
                ).formatted(Formatting.LIGHT_PURPLE)
            )
            tooltip.add(
                Text.translatable(
                    "quilt_energy.energyitem.stored",
                    formatDouble(stored(stack).toDouble(), 64)
                ).formatted(Formatting.LIGHT_PURPLE)
            )
        } else {
            tooltip.add(
                Text.translatable("quilt_energy.item.shift_to_expand").formatted(Formatting.LIGHT_PURPLE)
            )
        }
    }

    fun getTooltipDataDefault(stack: ItemStack): Optional<TooltipData> {
        return EnergyBarTooltipData.fromEnergizedItem(stack)
    }

    override fun stored(stack: ItemStack): BigDecimal {
        if (stack.nbt?.contains("stored") != true) {
            NbtHelper.writeToCompound(stack.nbt!!, "stored", BigDecimal("0"))
        }
        return NbtHelper.readFromCompound(stack.nbt!!, "stored")
    }

    override fun setEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult {
        val original: BigDecimal = stored(stack)
        if (amount < BigDecimal("0")) return EnergyInteractionResult(unit(), original, original, false)
        if (amount > getMaxCapacity()) return EnergyInteractionResult(unit(), original, original, false)
        NbtHelper.writeToCompound(stack.nbt!!, "stored", amount)
        return EnergyInteractionResult(unit(), original, amount, true)
    }

    override fun addEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult {
        val original: BigDecimal = stored(stack)
        if ((original + amount) > getMaxCapacity())
            return EnergyInteractionResult(unit(), original, original, false)
        setEnergy(stack, original + amount)
        return EnergyInteractionResult(unit(), original, original + amount, true)
    }

    override fun removeEnergy(stack: ItemStack, amount: BigDecimal): EnergyInteractionResult {
        val original: BigDecimal = stored(stack)
        if ((original - amount) < BigDecimal("0"))
            return EnergyInteractionResult(unit(), original, original, false)
        setEnergy(stack, original - amount)
        return EnergyInteractionResult(unit(), original, original - amount, true)
    }

    override fun <T : IEnergizedItem> transferEnergy(
        self: ItemStack,
        otherClass: T,
        otherStack: ItemStack,
        amount: BigDecimal,
        operation: IEnergyContainer.Operation
    ) {
        if (operation === IEnergyContainer.Operation.RECEIVE) {
            addEnergy(self, otherClass.unit().convertTo(unit(), amount))
            removeEnergy(otherStack, unit().convertTo(otherClass.unit(), amount))
        } else {
            removeEnergy(self, otherClass.unit().convertTo(unit(), amount))
            addEnergy(otherStack, unit().convertTo(otherClass.unit(), amount))
        }
    }

    override fun transferEnergy(
        self: ItemStack,
        other: IEnergyContainer,
        amount: BigDecimal,
        operation: IEnergyContainer.Operation
    ) {
        if (operation === IEnergyContainer.Operation.RECEIVE) {
            addEnergy(self, other.unit().convertTo(unit(), amount))
            other.removeEnergy(unit().convertTo(other.unit(), amount))
        } else {
            removeEnergy(self, other.unit().convertTo(unit(), amount))
            other.addEnergy(unit().convertTo(other.unit(), amount))
        }
    }

    @Suppress("Duplicates")
    override fun equalizeWith(self: ItemStack, containers: List<IEnergyContainer>, stacks: List<Pair<IEnergizedItem, ItemStack>>) {
        var total: BigDecimal = EnergyUnits.RAW_ENERGY.convertFrom(unit(), stored(self))
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

}