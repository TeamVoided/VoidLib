package org.teamvoided.voidlib.wfc.data.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.cresm.loader.CResMSingleLoader
import org.teamvoided.voidlib.wfc.WFC

class WaveFunctionDefinitionLoader : CResMSingleLoader {
    override fun loadResource(id: Identifier, manager: ResourceManager) {
        val functions = parseJson(id, manager).get("functions").asJsonArray
        val loader = WaveFunctionLoader()
        functions.forEach { functionIdEl ->
            val functionId = WFC.idFromString(functionIdEl.asString)
            loader.loadResource(Identifier(functionId.namespace, "${functionId.path}.json"), manager)
        }
    }
}