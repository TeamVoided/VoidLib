package org.teamvoided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.cresm.loader.CResMSingleLoader
import org.teamvoided.voidlib.wfc.WFC
import org.teamvoided.voidlib.wfc.WFCRegistries
import org.teamvoided.voidlib.wfc.wave.AdjacencyIndex
import org.teamvoided.voidlib.wfc.wave.Tile

class TileDefinitionLoader : CResMSingleLoader {
    override suspend fun loadResource(id: Identifier, manager: ResourceManager) {
        val json = parseJson(id, manager)
        val tiles = json.get("tiles").asJsonArray
        tiles.forEach { element ->
            val tileId = WFC.idFromString(element.asJsonObject.get("id").asString)
            val adjacencyIndexObject = element.asJsonObject.get("adjacency").asJsonObject
            val adjacencyIndex = AdjacencyIndex(
                adjacencyIndexObject.get("up").asInt,
                adjacencyIndexObject.get("right").asInt,
                adjacencyIndexObject.get("down").asInt,
                adjacencyIndexObject.get("left").asInt
            )
            val rotatable = element.asJsonObject.get("rotatable").asBoolean

            WFCRegistries.registerTile(tileId, Tile(tileId, adjacencyIndex, rotatable))
        }
    }
}