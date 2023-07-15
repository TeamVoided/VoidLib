package org.teamvoided.voidlib.wfc

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.voidlib.cresm.CResM
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.wfc.command.WaveFunctionArgumentType
import org.teamvoided.voidlib.wfc.data.JsonType
import org.teamvoided.voidlib.wfc.data.loader.WFCLoaders
import org.teamvoided.voidlib.wfc.wave.Tiles
import org.teamvoided.voidlib.wfc.wave.WaveFunction
import org.teamvoided.voidlib.wfc.wave.rule.LimitedPlacementRule
import org.teamvoided.voidlib.wfc.wave.rule.TileGenerationRuleType

object WFC {
    val LOGGER: Logger = LoggerFactory.getLogger("VoidLib: WFC")
    val LIMITED_PLACEMENT_TYPE: TileGenerationRuleType<LimitedPlacementRule> = TileGenerationRuleType(listOf(Pair("tile", JsonType.STRING), Pair("max_placements", JsonType.NUMBER))) { json ->
        val id = idFromString(json.get("tile").asString)
        val maxPlacements = json.get("maxPlacements").asInt

        val tile = WFCRegistries.TILE_REGISTRY[id] ?: throw IllegalStateException("no tile matching $id could be found")

        return@TileGenerationRuleType LimitedPlacementRule(tile, maxPlacements)
    }

    fun idFromString(string: String): Identifier {
        if (string.contains(":")) {
            val split = string.split(":")
            return Identifier(split[0], split[1])
        }

        return Identifier(string)
    }

    fun onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(id("wf_argument_type"), WaveFunctionArgumentType::class.java, ConstantArgumentSerializer.of(WaveFunctionArgumentType::waveFunction))

        WFCRegistries.registerTile(Tiles.BLANK.id, Tiles.BLANK)
        WFCRegistries.registerTile(Tiles.UP.id, Tiles.UP)
        WFCRegistries.registerTile(Tiles.RIGHT.id, Tiles.RIGHT)
        WFCRegistries.registerTile(Tiles.DOWN.id, Tiles.DOWN)
        WFCRegistries.registerTile(Tiles.LEFT.id, Tiles.LEFT)
        WFCRegistries.registerTileGenerationRuleType(
            id("limited_placement"),
            LIMITED_PLACEMENT_TYPE
        )
        CResM.registerLoader(WFCLoaders.TILE_DEFINITION_LOADER)
        CResM.registerLoader(WFCLoaders.WAVE_FUNCTION_DEFINITION_LOADER)
        CResM.updateLoaders()

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
                                                ), generationRules = listOf()
                                            ).collapse().generateStructure(it.source.world, it.source.entity!!.blockPos.add(0, -1, 0), BlockRotation.NONE, BlockMirror.NONE, 1.0f)
                                        }.start()
                                        return@executes 1
                                    } else return@executes 0
                                }
                            )
                        )
                    )
                ).build()
            dispatcher.root.addChild(wfcNode)

            val waveCallNode = CommandManager.literal("wavecall")
                .then(CommandManager.argument("wave", WaveFunctionArgumentType.waveFunction())
                    .executes {
                        if (it.source.entity?.hasPermissionLevel(4) == true) {
                            Thread {
                                WaveFunctionArgumentType.getWaveFunction(it, "wave")
                                    .collapse().generateStructure(
                                        it.source.world,
                                        it.source.entity!!.blockPos.add(0, -1, 0),
                                        BlockRotation.NONE,
                                        BlockMirror.NONE,
                                        1.0f
                                    )
                            }.start()
                        }
                        return@executes 1
                    }
                ).build()
            dispatcher.root.addChild(waveCallNode)
        }

        LOGGER.info("Loaded VoidLib: WFC")
    }
}

