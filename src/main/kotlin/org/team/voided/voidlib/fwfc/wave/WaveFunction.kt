package org.team.voided.voidlib.fwfc.wave

import org.team.voided.voidlib.core.datastructures.Vec2i
import org.team.voided.voidlib.fwfc.wave.rule.ITileGenerationRule
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import java.util.LinkedList
import kotlin.random.Random

data class WaveFunction(
    val width: Int, val height: Int,
    val startWith: Int = 1,
    val discardSaveAfter: Int = 10,
    val seed: Long = System.currentTimeMillis(),
    val tiles: List<Tile>,
    val generationRules: List<ITileGenerationRule> = LinkedList()
) {
    private val grid = TileGrid(Vec2i(width, height))
    private val random = Random(seed)
    private var tilesPlacedSinceLastSave = 0
    private var savePoint: SavePoint? = null

    fun collapse(): TileGrid {
        start(startWith)

        while (!isComplete()) {
            val lowestEntropy = lowestEntropy()
            if (lowestEntropy.first.isEmpty()) {
                restart()
                generationRules.forEach { it.onRestart(grid) }
            } else {
                lowestEntropy.second.tile = lowestEntropy.first.random(random)
                generationRules.forEach { it.onPlaceTile(grid, lowestEntropy.second) }
                tilesPlacedSinceLastSave++
                if (tilesPlacedSinceLastSave >= discardSaveAfter) {
                    savePoint = SavePoint(grid.copy())
                    tilesPlacedSinceLastSave = 0
                }
            }
        }

        return grid.copy()
    }

    fun generate(world: ServerWorld, pos: BlockPos, rotation: BlockRotation, mirror: BlockMirror, integrity: Float, seed: Long = System.currentTimeMillis()) {
        start()

        while (!isComplete()) {
            val lowestEntropy = lowestEntropy()
            if (lowestEntropy.first.isEmpty()) {
                restart()
                generationRules.forEach { it.onRestart(grid) }
            } else {
                lowestEntropy.second.tile = lowestEntropy.first.random(random)
                generationRules.forEach { it.onPlaceTile(grid, lowestEntropy.second) }
                grid.generateStructure(world, pos, rotation, mirror, integrity, seed)

                tilesPlacedSinceLastSave++
                if (tilesPlacedSinceLastSave >= discardSaveAfter) {
                    savePoint = SavePoint(grid.copy())
                    tilesPlacedSinceLastSave = 0
                }
            }
        }
    }

    private fun lowestEntropy(): Pair<List<Tile>, PositionalTile> {
        val entropies: MutableMap<PositionalTile, Pair<List<Tile>, PositionalTile>> = LinkedHashMap()
        grid.forEachPositionalTile {
            if (!it.isComplete()) {
                val options = it.calculateOptions(grid, tiles)
                entropies[it] = Pair(options, it)
            }
        }

        var sorted = entropies.values.sortedBy { pair -> pair.first.size }

        val lookFor = sorted[0].first.size
        sorted = sorted.filter {
            it.first.size == lookFor
        }

        sorted = sorted.filter {
            var rulesMatch = true

            generationRules.forEach { rule ->
                if (!rule.computeRule(grid, it.second)) {
                    rulesMatch = false
                }
            }

            rulesMatch
        }

        return sorted.random(random)
    }

    private fun isComplete(): Boolean {
        var complete = true

        grid.forEachPositionalTile {
            if (!it.isComplete()) complete = false
        }

        return complete
    }

    private fun start(startCount: Int = 1) {
        for (i in 0 until startCount+1) {
            grid.randomPositionalTile(random).tile = tiles.random(random)
            tilesPlacedSinceLastSave += 1
        }
    }

    private fun restart() {
        if (savePoint != null && savePoint!!.loadCounter <= discardSaveAfter) {
            savePoint!!.load(grid)
            return
        }

        grid.reset()
        start(startWith)
    }

    data class SavePoint(val grid: TileGrid, var loadCounter: Int = 0) {
        fun load(grid: TileGrid) {
            this.grid.copyTo(grid)
            loadCounter++
        }
    }
}
