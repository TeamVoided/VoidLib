package org.teamvoided.voidlib.pow.item

import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import org.teamvoided.voidlib.pow.energy.EnergyUnit
import java.math.BigDecimal
import java.util.*

class EnergizedBlockItem(block: Block, settings: Settings, private var unit: EnergyUnit, private var maxCapacity: BigDecimal) :
    BlockItem(block, settings), StandardEnergizedItem {
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        appendHoverTextDefault(stack, world, tooltip, context)
    }

    override fun getTooltipData(stack: ItemStack): Optional<TooltipData> {
        return getTooltipDataDefault(stack)
    }

    override fun setUnit(unit: EnergyUnit) {
        this.unit = unit
    }

    override fun unit(): EnergyUnit {
        return unit
    }

    override fun setMaxCapacity(maxCapacity: BigDecimal) {
        this.maxCapacity = maxCapacity
    }

    override fun getMaxCapacity(): BigDecimal {
        return maxCapacity
    }
}