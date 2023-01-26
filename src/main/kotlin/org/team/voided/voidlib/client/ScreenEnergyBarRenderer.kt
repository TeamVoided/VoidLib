package org.team.voided.voidlib.client

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.TooltipComponent
import org.team.voided.voidlib.pow.client.gui.EnergyBarTooltipComponent

object ScreenEnergyBarRenderer {
    fun Screen.renderEnergyBar(components: List<TooltipComponent>, x: Int, y: Int) {
        for (component in components) {
            if (component is EnergyBarTooltipComponent) {
                component.setContext(x, y, width)
            }
        }
    }
}