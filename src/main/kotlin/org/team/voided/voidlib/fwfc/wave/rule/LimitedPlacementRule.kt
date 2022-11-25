package org.team.voided.voidlib.fwfc.wave.rule

import org.team.voided.voidlib.fwfc.wave.PositionalTile
import org.team.voided.voidlib.fwfc.wave.Tile
import org.team.voided.voidlib.fwfc.wave.TileGrid

class LimitedPlacementRule(val tile: Tile, val maxPlacements: Int): ITileGenerationRule {
    private var placements = 0;

    override fun computeRule(grid: TileGrid, tile: PositionalTile): Boolean = (placements < maxPlacements)

    override fun onPlaceTile(grid: TileGrid, tile: PositionalTile) {
        if (tile.tile == this.tile) {
            placements++
        }
    }

    override fun onRestart(grid: TileGrid) {
        placements = 0
    }
}