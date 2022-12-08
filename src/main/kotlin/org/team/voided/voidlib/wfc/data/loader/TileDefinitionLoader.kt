package org.team.voided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.team.voided.voidlib.cresm.loader.CResMSingleLoader
import org.team.voided.voidlib.wfc.WFC
import org.team.voided.voidlib.wfc.WFCRegistries
import org.team.voided.voidlib.wfc.wave.AdjacencyIndex
import org.team.voided.voidlib.wfc.wave.Tile

class TileDefinitionLoader : CResMSingleLoader {
    override fun loadResource(id: Identifier, manager: ResourceManager) {
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

            WFCRegistries.registerTile(tileId, Tile(tileId, adjacencyIndex))
            WFC.LOGGER.info("Registered tile with id $tileId and adjacency index [${adjacencyIndex.up}, ${adjacencyIndex.right}, ${adjacencyIndex.down}, ${adjacencyIndex.left}]")
        }
    }
}