package org.teamvoided.voidlib.wfc.wave

import net.minecraft.util.BlockRotation
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import java.util.*

data class PositionalTile(val position: Vec2i, var tile: Tile?) {
    private var rotation = BlockRotation.NONE

    fun rotated(rotation: BlockRotation): PositionalTile =
        PositionalTile(position, tile?.rotated(rotation)).setRotation(rotation)

    private fun setRotation(rotation: BlockRotation): PositionalTile {
        this.rotation = rotation
        return this
    }

    fun getRotation() = rotation

    fun isRotatable() = if (tile == null) false else tile!!.rotatable

    fun isComplete(): Boolean = tile != null

    fun calculateEntropy(grid: TileGrid, uniqueTiles: List<Tile>): Int {
        return calculateOptions(grid, uniqueTiles).size
    }

    fun calculateOptions(grid: TileGrid, uniqueTiles: List<Tile>): List<Tile> {
        val options: MutableList<Tile> = LinkedList()

        val up = grid.getPositionalTileAt(position + Vec2i(0, -1))
        val down = grid.getPositionalTileAt(position + Vec2i(0, 1))
        val left = grid.getPositionalTileAt(position + Vec2i(-1, 0))
        val right = grid.getPositionalTileAt(position + Vec2i(1, 0))

        uniqueTiles.forEach {
            var accept = true

            if (up?.tile != null && !(it.acceptAbove(up.tile!!) && up.tile!!.acceptBelow(it))) accept = false

            if (down?.tile != null && !(it.acceptBelow(down.tile!!) && down.tile!!.acceptAbove(it))) accept = false

            if (left?.tile != null && !(it.acceptLeft(left.tile!!) && left.tile!!.acceptRight(it))) accept = false

            if (right?.tile != null && !(it.acceptRight(right.tile!!) && right.tile!!.acceptLeft(it))) accept = false

            if (accept) options.add(it)
        }

        return options
    }
}