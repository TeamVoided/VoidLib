package com.team.voided.voidlib.fwfc.wave

import com.team.voided.voidlib.core.datastructures.Vec2i
import java.util.LinkedList

data class PositionalTile(val position: Vec2i, var tile: Tile?) {
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

            if (up?.tile != null) {
                if (!(it.acceptAbove(up.tile!!) && up.tile!!.acceptBelow(it))) accept = false
            }

            if (down?.tile != null) {
                if (!(it.acceptBelow(down.tile!!) && down.tile!!.acceptAbove(it))) accept = false
            }

            if (left?.tile != null) {
                if (!(it.acceptLeft(left.tile!!) && left.tile!!.acceptRight(it))) accept = false
            }

            if (right?.tile != null) {
                if (!(it.acceptRight(right.tile!!) && right.tile!!.acceptLeft(it))) accept = false
            }

            if (accept) options.add(it)
        }

        return options
    }
}