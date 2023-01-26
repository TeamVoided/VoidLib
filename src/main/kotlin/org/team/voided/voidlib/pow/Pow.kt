package org.team.voided.voidlib.pow

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.pow.client.gui.EnergyBarTooltipComponent
import org.team.voided.voidlib.pow.command.SetEnergyCommand
import org.team.voided.voidlib.pow.energy.EnergyUnits
import java.text.DecimalFormat

class Pow : LibModule("Pow") {
    companion object {
        val LOGGER = LoggerFactory.getLogger("Quilt Energy")
        const val MOD_ID = "quilt_energy"
        fun modLoc(path: String?): Identifier {
            return Identifier(MOD_ID, path)
        }

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
        println("cool")
    }
}