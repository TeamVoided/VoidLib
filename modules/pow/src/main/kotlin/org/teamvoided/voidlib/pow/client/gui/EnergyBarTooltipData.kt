package org.teamvoided.voidlib.pow.client.gui

import net.minecraft.client.item.TooltipData
import net.minecraft.item.ItemStack
import org.teamvoided.voidlib.pow.energy.EnergyUnit
import org.teamvoided.voidlib.pow.item.IEnergizedItem
import java.util.*

class EnergyBarTooltipData(val percentFull: Float, unit: EnergyUnit) : TooltipData {
    private val unit: EnergyUnit

    init {
        this.unit = unit
    }

    fun getUnit(): EnergyUnit {
        return unit
    }

    companion object {
        fun fromEnergizedItem(stack: ItemStack): Optional<TooltipData> {
            val item = stack.item
            return if (item is IEnergizedItem) {
                Optional.of(
                    EnergyBarTooltipData(
                        getFractionForDisplay(
                            item,
                            stack
                        ), item.unit()
                    )
                )
            } else Optional.empty<TooltipData>()
        }

        fun getFractionForDisplay(item: IEnergizedItem, stack: ItemStack): Float {
            return (item.stored(stack) / item.getMaxCapacity()).toFloat()
        }
    }
}