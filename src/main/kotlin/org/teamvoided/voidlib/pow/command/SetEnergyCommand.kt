package org.teamvoided.voidlib.pow.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.item.ItemStack
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import org.teamvoided.voidlib.pow.Pow.Companion.formatDouble
import org.teamvoided.voidlib.pow.item.IEnergizedItem
import java.math.BigDecimal

object SetEnergyCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("setenergy")
            .requires { it.hasPermissionLevel(2) }
            .then(
                CommandManager.argument("targets", EntityArgumentType.players())
                    .then(CommandManager.argument("energy", DoubleArgumentType.doubleArg())
                        .executes { source: CommandContext<ServerCommandSource> ->
                            setEnergy(
                                source
                            )
                        })
            )
        )
    }

    @Throws(CommandSyntaxException::class)
    private fun setEnergy(source: CommandContext<ServerCommandSource>): Int {
        val players: Collection<ServerPlayerEntity> = EntityArgumentType.getPlayers(source, "targets")
        val energy: Double = DoubleArgumentType.getDouble(source, "energy")
        for (player in players) {
            val stackInHand: ItemStack = player.getStackInHand(Hand.MAIN_HAND)
            val item = stackInHand.item
            if (item !is IEnergizedItem) {
                source.source.sendError(
                    Text.translatable(
                        "commands.set.energy.not_energized_item",
                        player.name.string
                    )
                )
                return 0
            }
            item.setEnergy(stackInHand, BigDecimal(energy.toString()))
        }
        source.source.sendFeedback(
            Text.translatable("commands.set.energy.success", formatDouble(energy, 64), players.size), true
        )
        return 1
    }
}