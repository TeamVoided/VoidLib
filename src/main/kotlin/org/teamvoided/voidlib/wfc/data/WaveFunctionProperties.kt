package org.teamvoided.voidlib.wfc.data

import org.teamvoided.voidlib.wfc.wave.Tile
import org.teamvoided.voidlib.wfc.wave.WaveFunction
import org.teamvoided.voidlib.wfc.wave.rule.ITileGenerationRule

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
