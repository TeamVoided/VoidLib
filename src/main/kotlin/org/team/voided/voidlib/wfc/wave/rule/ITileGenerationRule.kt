package org.team.voided.voidlib.wfc.wave.rule

import org.team.voided.voidlib.wfc.wave.PositionalTile
import org.team.voided.voidlib.wfc.wave.TileGrid

interface ITileGenerationRule<T: ITileGenerationRule<T>> {
    fun computeRule(grid: TileGrid, tile: PositionalTile): Boolean
    fun onPlaceTile(grid: TileGrid, tile: PositionalTile)
    fun onRestart(grid: TileGrid)

    fun getType(): TileGenerationRuleType<T>
}