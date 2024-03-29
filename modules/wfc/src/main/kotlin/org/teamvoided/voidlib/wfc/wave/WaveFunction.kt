package org.teamvoided.voidlib.wfc.wave

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.wfc.wave.rule.ITileGenerationRule
import java.util.*
import kotlin.random.Random

data class WaveFunction(
    val width: Int, val height: Int,
    val startWith: Int = 1,
    val discardSaveAfter: Int = 10,
    var seed: Long = System.currentTimeMillis(),
    val tiles: List<Tile>,
    val generationRules: List<ITileGenerationRule<*>> = LinkedList()
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

    fun reseed(): WaveFunction {
        seed = System.currentTimeMillis()
        return this
    }

    fun setSeed(seed: Long): WaveFunction {
        this.seed = seed
        return this
    }

    private fun lowestEntropy(): Pair<List<Tile>, PositionalTile> {
        var entropies: MutableMap<PositionalTile, Pair<List<Tile>, PositionalTile>> = LinkedHashMap()
        grid.forEachPositionalTile {
            if (!it.isComplete()) {
                val options = LinkedList<Tile>()

                if (it.isRotatable()) {
                    for (rotation in BlockRotation.values()) {
                        options.addAll(it.rotated(rotation).calculateOptions(grid, tiles))
                    }
                } else {
                    options.addAll(it.calculateOptions(grid, tiles))
                }

                entropies[it] = Pair(options, it)
            }
        }

        entropies = entropies.filter { (_, pair) ->
            var rulesMatch = true

            generationRules.forEach { rule ->
                if (!rule.computeRule(grid, pair.second)) {
                    rulesMatch = false
                }
            }

            rulesMatch
        }.toMutableMap()

        var sorted = entropies.values.sortedBy { pair -> pair.first.size }

        val lookFor = sorted[0].first.size
        sorted = sorted.filter {
            it.first.size == lookFor
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
        for (i in 0 until startCount + 1) {
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
