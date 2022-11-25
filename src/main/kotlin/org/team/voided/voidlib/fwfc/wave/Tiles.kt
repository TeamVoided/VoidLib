package org.team.voided.voidlib.fwfc.wave

import org.team.voided.voidlib.id

class Tiles {
    companion object {
        val BLANK = Tile(id("blank_tile"), Tile.EMPTY.adjacencyIndex)
        val UP = Tile(
            id("up_tile"),
            AdjacencyIndex(1, 0, 1, 1)
        )
        val RIGHT = Tile(
            id("right_tile"),
            AdjacencyIndex(1, 1, 0, 1)
        )
        val DOWN = Tile(
            id("down_tile"),
            AdjacencyIndex(0, 1, 1, 1)
        )
        val LEFT = Tile(
            id("left_tile"),
            AdjacencyIndex(1, 1, 1, 0)
        )
    }
}