package com.team.voided.voidlib.fwfc

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import com.team.voided.voidlib.LOGGER
import com.team.voided.voidlib.core.LibModule
import com.team.voided.voidlib.fwfc.wave.Tiles
import com.team.voided.voidlib.fwfc.wave.WaveFunction
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation

class FWFC: LibModule("fwfc") {
    override fun commonSetup() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            val wfcNode: LiteralCommandNode<ServerCommandSource> = CommandManager
                .literal("wfctest")
                .then(CommandManager.argument("width", IntegerArgumentType.integer(1))
                    .then(CommandManager.argument("height", IntegerArgumentType.integer(1))
                        .then(CommandManager.argument("startingTiles", IntegerArgumentType.integer(1))
                            .then(CommandManager.argument("discardSaveAfter", IntegerArgumentType.integer(1))
                                .executes {
                                    if (it.source.entity?.hasPermissionLevel(4) == true) {
                                        Thread {
                                            WaveFunction(
                                                IntegerArgumentType.getInteger(it, "width"),
                                                IntegerArgumentType.getInteger(it, "height"),
                                                IntegerArgumentType.getInteger(it, "startingTiles"),
                                                IntegerArgumentType.getInteger(it, "discardSaveAfter"),
                                                tiles = listOf(
                                                    Tiles.BLANK, Tiles.UP, Tiles.DOWN, Tiles.LEFT, Tiles.RIGHT
                                                )).collapse().generateStructure(it.source.world, it.source.entity!!.blockPos, BlockRotation.NONE, BlockMirror.NONE, 1.0f)
                                        }.start()
                                        return@executes 1
                                    } else return@executes 0
                                }
                            )
                        )
                    )
                ).build()
            dispatcher.root.addChild(wfcNode)
        }
        LOGGER.info("Loaded VoidLib: FWFC")
    }

    override fun clientSetup() { }
}

