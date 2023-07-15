package org.teamvoided.voidlib.pow.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import org.teamvoided.voidlib.pow.energy.EnergyUnit
import java.math.BigDecimal
import java.util.*

class EnergizedItem(properties: Settings?, private var unit: EnergyUnit, private var maxCapacity: BigDecimal) :
    Item(properties),
    StandardEnergizedItem {
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