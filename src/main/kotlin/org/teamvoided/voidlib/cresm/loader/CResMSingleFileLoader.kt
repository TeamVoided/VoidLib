package org.teamvoided.voidlib.cresm.loader

import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier

open class CResMSingleFileLoader(
    protected val folder: String,
    protected val fullFileName: String,
    private val resourceType: ResourceType,
    private val fabricId: Identifier,
    protected val singleLoader: CResMSingleLoader
) : ICResMLoader {
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