package org.team.voided.voidlib.wfc.data

import org.team.voided.voidlib.wfc.wave.Tile
import org.team.voided.voidlib.wfc.wave.WaveFunction
import org.team.voided.voidlib.wfc.wave.rule.ITileGenerationRule

data class WaveFunctionProperties(
    val width: Int,
    val height: Int,
    val startingTiles: Int,
    val discardSaveAfter: Int,
    val tiles: List<Tile>,
    val generationRules: List<ITileGenerationRule<*>>
) {
    fun createWaveFunction(): WaveFunction {
        return WaveFunction(
            width = width,
            height = height,
            startWith = startingTiles,
            discardSaveAfter = discardSaveAfter,
            tiles = tiles,
            generationRules = generationRules
        )
    }
}
