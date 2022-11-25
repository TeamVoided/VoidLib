package com.team.voided.voidlib.fwfc.wave.rule

import com.team.voided.voidlib.fwfc.wave.PositionalTile
import com.team.voided.voidlib.fwfc.wave.TileGrid

interface ITileGenerationRule {
    fun computeRule(grid: TileGrid, tile: PositionalTile): Boolean
    fun onPlaceTile(grid: TileGrid, tile: PositionalTile)
    fun onRestart(grid: TileGrid)
}