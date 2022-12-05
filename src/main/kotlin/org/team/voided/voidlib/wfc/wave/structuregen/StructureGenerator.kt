package org.team.voided.voidlib.wfc.wave.structuregen

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.block.entity.StructureBlockBlockEntity
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.server.world.ServerWorld
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.structure.processor.BlockRotStructureProcessor
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.InvalidIdentifierException
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3i
import org.team.voided.voidlib.wfc.wave.Tile
import java.util.*

class StructureGenerator(val tileGrid: Array<Array<Tile?>>) {
    fun generate(world: ServerWorld, pos: BlockPos, rotation: BlockRotation, mirror: BlockMirror, integrity: Float, seed: Long = System.currentTimeMillis()) {
        var zShift = 0
        var xShift = 0

        tileGrid.forEach {
            var lastTemplateZSize = 0
            it.forEach { tile ->
                if (tile == null) return
                val shiftedPos = pos.add(Vec3i(-xShift, 0,-zShift))
                val optionalTemplate: Optional<StructureTemplate>?
                try {
                    optionalTemplate = world.structureTemplateManager.getTemplate(tile.id)
                } catch (ex: InvalidIdentifierException) {
                    ex.printStackTrace()
                    return
                }

                optionalTemplate.ifPresent { template ->
                    throwOnUnloadedPos(world, ChunkPos(shiftedPos), ChunkPos(shiftedPos.add(template.size)))
                    val placementData = StructurePlacementData().setMirror(mirror).setRotation(rotation)
                    if (integrity < 1.0) {
                        placementData.clearProcessors().addProcessor(BlockRotStructureProcessor(integrity)).setRandom(StructureBlockBlockEntity.createRandom(seed))
                    }
                    template.place(world, shiftedPos, shiftedPos, placementData, StructureBlockBlockEntity.createRandom(seed), 2)
                    lastTemplateZSize = template.size.z
                    xShift += template.size.x
                }
            }
            zShift += lastTemplateZSize
            xShift = 0
        }
    }

    @Throws(CommandSyntaxException::class)
    private fun throwOnUnloadedPos(world: ServerWorld, pos1: ChunkPos, pos2: ChunkPos) {
        if (ChunkPos.stream(pos1, pos2).filter { pos: ChunkPos ->
                !world.canSetBlock(pos.startPos)
            }.findAny().isPresent) {
            throw BlockPosArgumentType.UNLOADED_EXCEPTION.create()
        }
    }
}