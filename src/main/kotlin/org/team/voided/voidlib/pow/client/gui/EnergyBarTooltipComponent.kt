package org.team.voided.voidlib.pow.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
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

    override fun drawItems(renderer: TextRenderer?, x: Int, y: Int, matrices: MatrixStack, itemRenderer: ItemRenderer?, z: Int) {
        val height = 3
        val offsetFromBox = 4
        matrices.push()
        matrices.translate(0.0, 0.0, z.toDouble())
        val barWidth = ceil((totalWidth * percentFull).toDouble()).toInt()
        val color = unit.getEnergyBarColor()
        val colorCode: Int = color.red and 0x0ff shl 16 or (color.green and 0x0ff shl 8) or (color.blue and 0x0ff)
        DrawableHelper.fill(
            matrices,
            mouseX - 1,
            mouseY - height - offsetFromBox - 1,
            mouseX + totalWidth + 1,
            mouseY - offsetFromBox,
            -0x1000000
        )
        DrawableHelper.fill(
            matrices,
            mouseX,
            mouseY - height - offsetFromBox,
            mouseX + barWidth,
            mouseY - offsetFromBox,
            -0x1000000 or colorCode
        )
        DrawableHelper.fill(
            matrices,
            mouseX + barWidth,
            mouseY - height - offsetFromBox,
            mouseX + totalWidth,
            mouseY - offsetFromBox,
            -0xaaaaab
        )
        mouseY = 0
        mouseX = mouseY
        totalWidth = 50
    }

    companion object {
        fun tryConvert(component: TooltipData?): TooltipComponent? {
            if (component == null) return null
            return if (component is EnergyBarTooltipData) EnergyBarTooltipComponent(component) else null
        }
    }
}