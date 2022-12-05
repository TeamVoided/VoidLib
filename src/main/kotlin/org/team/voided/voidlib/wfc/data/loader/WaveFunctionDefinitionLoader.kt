package org.team.voided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.team.voided.voidlib.cresm.loader.CResMSingleLoader
import org.team.voided.voidlib.wfc.WFC

class WaveFunctionDefinitionLoader: CResMSingleLoader {
    override fun loadResource(id: Identifier, manager: ResourceManager) {
        val functions = parseJson(id, manager).get("functions").asJsonArray
        val loader = WaveFunctionLoader()
        functions.forEach { functionIdEl ->
            val functionId = WFC.idFromString(functionIdEl.asString)
            loader.loadResource(Identifier(functionId.namespace, "${functionId.path}.json"), manager)
        }
    }
}