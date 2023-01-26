package org.team.voided.voidlib.wfc

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import net.minecraft.util.Identifier
import org.team.voided.voidlib.wfc.data.WaveFunctionProperties
import org.team.voided.voidlib.wfc.wave.Tile
import org.team.voided.voidlib.wfc.wave.rule.ITileGenerationRule
import org.team.voided.voidlib.wfc.wave.rule.TileGenerationRuleType

class WFCRegistries {
    companion object {
        val TILE_REGISTRY: BiMap<Identifier, Tile> = HashBiMap.create()
        val TILE_GENERATION_RULE_TYPE: BiMap<Identifier, TileGenerationRuleType<*>> = HashBiMap.create()
        val WAVE_FUNCTION: BiMap<Identifier, WaveFunctionProperties> = HashBiMap.create()

        fun registerTile(id: Identifier, tile: Tile): Tile {
            TILE_REGISTRY[id] = tile
            return tile
        }

        fun <A : ITileGenerationRule<A>, T : TileGenerationRuleType<A>> registerTileGenerationRuleType(
            id: Identifier,
            type: T
        ): T {
            TILE_GENERATION_RULE_TYPE[id] = type
            return type
        }

        fun registerWaveFunction(id: Identifier, function: WaveFunctionProperties): WaveFunctionProperties {
            WAVE_FUNCTION[id] = function
            return function
        }
    }
}