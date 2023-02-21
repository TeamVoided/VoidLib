package org.teamvoided.voidlib.pow

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.pow.client.gui.EnergyBarTooltipComponent
import org.teamvoided.voidlib.pow.command.SetEnergyCommand
import org.teamvoided.voidlib.pow.energy.EnergyUnits
import java.text.DecimalFormat

class Pow : LibModule("Pow") {
    companion object {
        fun formatDouble(d: Double, maximumFractionalDigits: Int): String {
            val format = DecimalFormat()
            format.maximumFractionDigits = maximumFractionalDigits
            return format.format(d)
        }
    }

    override fun commonSetup() {
        EnergyUnits.register()
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            SetEnergyCommand.register(
                dispatcher
            )
        }
    }

    override fun clientSetup() {
        TooltipComponentCallback.EVENT.register(EnergyBarTooltipComponent::tryConvert)
    }
}