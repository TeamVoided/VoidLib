package org.team.voided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceType
import org.team.voided.voidlib.cresm.loader.CResMSingleFileLoader
import org.team.voided.voidlib.id

class WFCLoaders {
    companion object {
        val TILE_DEFINITION_LOADER = CResMSingleFileLoader(
            "wfc",
            "tiles.json",
            ResourceType.SERVER_DATA,
            id("tile_definition_loader"),
            TileDefinitionLoader()
        )

        val WAVE_FUNCTION_DEFINITION_LOADER = CResMSingleFileLoader(
            "wfc",
            "functions.json",
            ResourceType.SERVER_DATA,
            id("wf_definitions_loader"),
            WaveFunctionDefinitionLoader()
        )
    }
}