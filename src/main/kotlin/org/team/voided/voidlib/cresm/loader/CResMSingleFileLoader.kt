package org.team.voided.voidlib.cresm.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import java.util.function.Predicate

class CResMSingleFileLoader(
    private val folder: String,
    private val fullFileName: String,
    private val resourceType: ResourceType,
    private val fabricId: Identifier,
    private val singleLoader: CResMSingleLoader
): ICResMLoader {
    override fun reload(manager: ResourceManager) {
        manager.findResources(folder) { it.path == "$folder/$fullFileName" }.forEach { (id, _) ->
            singleLoader.loadResource(id, manager)
        }
    }

    override fun getFabricId(): Identifier {
        return fabricId
    }

    override fun getType(): ResourceType {
        return resourceType
    }
}