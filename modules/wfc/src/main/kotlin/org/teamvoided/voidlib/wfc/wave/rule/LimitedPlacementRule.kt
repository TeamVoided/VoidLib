package org.teamvoided.voidlib.wfc.wave.rule

import org.teamvoided.voidlib.wfc.WFC
import org.teamvoided.voidlib.wfc.wave.PositionalTile
import org.teamvoided.voidlib.wfc.wave.Tile
import org.teamvoided.voidlib.wfc.wave.TileGrid

class LimitedPlacementRule(val tile: Tile, val maxPlacements: Int) : ITileGenerationRule<LimitedPlacementRule> {
    private var placements = 0

    override fun computeRule(grid: TileGrid, tile: PositionalTile): Boolean = (placements < maxPlacements)

    override fun onPlaceTile(grid: TileGrid, tile: PositionalTile) {
        if (tile.tile == this.tile) {
            placements++
        }
    }

    override fun onRestart(grid: TileGrid) {
        placements = 0
    }

    override fun getType(): TileGenerationRuleType<LimitedPlacementRule> {
        return WFC.LIMITED_PLACEMENT_TYPE
    }
}