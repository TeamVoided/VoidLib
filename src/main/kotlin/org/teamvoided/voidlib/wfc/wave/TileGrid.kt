package org.teamvoided.voidlib.wfc.wave

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.wfc.wave.structuregen.StructureGenerator
import kotlin.random.Random

data class TileGrid(val dimensions: Vec2i, val init: (Vec2i) -> PositionalTile = { pos -> PositionalTile(pos, null) }) {
    val underlyingArray: Array<Array<PositionalTile>> = Array(dimensions.y) { y ->
        Array(dimensions.x) { x ->
            init(Vec2i(x, y))
        }
    }

    fun getTileAt(position: Vec2i): Tile? {
        return try {
            underlyingArray[position.y][position.x].tile
        } catch (_: IndexOutOfBoundsException) {
            null
        }
    }

    fun setTileAt(position: Vec2i, tile: Tile) {
        if (position.y >= dimensions.y || position.x >= dimensions.x) return
        underlyingArray[position.y][position.x].tile = tile
    }

    fun getPositionalTileAt(position: Vec2i): PositionalTile? {
        return try {
            underlyingArray[position.y][position.x]
        } catch (_: IndexOutOfBoundsException) {
            null
        }
    }

    fun setPositionalTile(tile: PositionalTile) {
        if (tile.position.y >= dimensions.y || tile.position.x >= dimensions.x) return
        underlyingArray[tile.position.y][tile.position.x].tile = tile.tile
    }

    fun forEachPositionalTile(func: (PositionalTile) -> Unit) {
        underlyingArray.forEach {
            it.forEach(func)
        }
    }

    fun randomPositionalTile(random: Random): PositionalTile = underlyingArray.random(random).random(random)

    fun subGrid(dimensions: Vec2i, offset: Vec2i = Vec2i(0, 0)): TileGrid? {
        if (dimensions > this.dimensions) return null
        val subGrid = TileGrid(dimensions)

        for (y in offset.y until dimensions.y) {
            for (x in offset.x until dimensions.x) {
                subGrid.setPositionalTile(underlyingArray[y][x])
            }
        }

        return subGrid
    }

    fun copy(): TileGrid = subGrid(dimensions)!!

    fun copyTo(other: TileGrid) {
        other.reset()
        forEachPositionalTile {
            other.setPositionalTile(it)
        }
    }

    fun reset() {
        underlyingArray.forEach {
            it.forEach { tile ->
                val y = underlyingArray.indexOf(it)
                val x = it.indexOf(tile)
                underlyingArray[y][x] = init(Vec2i(x, y))
            }
        }
    }

    fun generateStructure(world: ServerWorld, pos: BlockPos, rotation: BlockRotation, mirror: BlockMirror, integrity: Float, seed: Long = System.currentTimeMillis()) {
        StructureGenerator(toTileTwoDimArray()).generate(world, pos, rotation, mirror, integrity, seed)
    }

    fun toTileTwoDimArray(): Array<Array<Tile?>> {
        return Array(dimensions.y) { y -> Array(dimensions.x) { x -> underlyingArray[y][x].tile } }
    }
}