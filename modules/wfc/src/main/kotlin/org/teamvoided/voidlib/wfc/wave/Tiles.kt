package org.teamvoided.voidlib.wfc.wave

import org.teamvoided.voidlib.core.id

class Tiles {
    companion object {
        val BLANK = Tile(id("blank_tile"), Tile.EMPTY.adjacencyIndex, false)
        val UP = Tile(
            id("up_tile"),
            AdjacencyIndex(1, 1, 0, 1),
            false
        )
        val RIGHT = Tile(
            id("right_tile"),
            AdjacencyIndex(1, 1, 1, 0),
            false
        )
        val DOWN = Tile(
            id("down_tile"),
            AdjacencyIndex(0, 1, 1, 1),
            false
        )
        val LEFT = Tile(
            id("left_tile"),
            AdjacencyIndex(1, 0, 1, 1),
            false
        )
    }
}