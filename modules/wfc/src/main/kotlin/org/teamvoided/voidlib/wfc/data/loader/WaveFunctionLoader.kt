package org.teamvoided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.cresm.loader.CResMSingleLoader
import org.teamvoided.voidlib.wfc.WFC
import org.teamvoided.voidlib.wfc.WFCRegistries
import org.teamvoided.voidlib.wfc.data.WaveFunctionProperties
import org.teamvoided.voidlib.wfc.wave.rule.ITileGenerationRule
import java.util.*

class WaveFunctionLoader : CResMSingleLoader {
    override suspend fun loadResource(id: Identifier, manager: ResourceManager) {
        val json = parseJson(Identifier(id.namespace, "functions/${id.path}"), manager)

        val fId = WFC.idFromString(json.get("id").asString)
        val width = json.get("width").asInt
        val height = json.get("height").asInt
        val startingTiles = json.get("startingTiles").asInt
        val discardSaveAfter = json.get("discardSaveAfter").asInt
        val tiles = json.get("tiles").asJsonArray.map {
            WFCRegistries.TILE_REGISTRY[WFC.idFromString(it.asString)]!!
        }
        val generationRules: MutableList<ITileGenerationRule<*>> = LinkedList()
        json.get("generationRules").asJsonArray.forEach {
            val type = WFCRegistries.TILE_GENERATION_RULE_TYPE[WFC.idFromString(it.asJsonObject.get("type").asString)]!!
            generationRules.add(type.construct(it.asJsonObject))
        }

        WFCRegistries.registerWaveFunction(
            fId,
            WaveFunctionProperties(width, height, startingTiles, discardSaveAfter, tiles, generationRules)
        )
    }
}