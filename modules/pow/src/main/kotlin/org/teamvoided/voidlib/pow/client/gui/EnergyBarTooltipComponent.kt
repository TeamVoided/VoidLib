package org.teamvoided.voidlib.pow.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData
import kotlin.math.ceil

class EnergyBarTooltipComponent(component: EnergyBarTooltipData) : TooltipComponent {
    private val percentFull = component.percentFull
    private val unit = component.getUnit()
    private var mouseX = 0
    private var mouseY = 0
    private var totalWidth = 0

    override fun getHeight(): Int {
        return 0
    }

    override fun getWidth(renderer: TextRenderer): Int {
        return 0
    }

    fun setContext(mouseX: Int, mouseY: Int, totalWidth: Int) {
        this.mouseX = mouseX
        this.mouseY = mouseY
        this.totalWidth = totalWidth
    }

    override fun drawItems(renderer: TextRenderer, x: Int, y: Int, drawContext: DrawContext) {
        val height = 3
        val offsetFromBox = 4
        drawContext.matrices.push()
        drawContext.matrices.translate(0.0, 0.0, 0.0)
        val barWidth = ceil((totalWidth * percentFull).toDouble()).toInt()
        val color: Int = unit.getEnergyBarColor().toInt()
        drawContext.fill(
            mouseX - 1,
            mouseY - height - offsetFromBox - 1,
            mouseX + totalWidth + 1,
            mouseY - offsetFromBox,
            -0x1000000
        )
        drawContext.fill(
            mouseX,
            mouseY - height - offsetFromBox,
            mouseX + barWidth,
            mouseY - offsetFromBox,
            -0x1000000 or color
        )
        drawContext.fill(
            mouseX + barWidth,
            mouseY - height - offsetFromBox,
            mouseX + totalWidth,
            mouseY - offsetFromBox,
            -0xaaaaab
        )
        mouseY = 0
        mouseX = 0
        totalWidth = 50
    }

    companion object {
        fun tryConvert(component: TooltipData?): TooltipComponent? {
            if (component == null) return null
            return if (component is EnergyBarTooltipData) EnergyBarTooltipComponent(component) else null
        }
    }
}