package org.teamvoided.voidlib.cresm

import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import org.slf4j.LoggerFactory
import org.teamvoided.voidlib.cresm.loader.ICResMLoader
import java.util.*

object CResM {
    val LOGGER = LoggerFactory.getLogger("VoidLib: CResM")

    private val loaders: MutableList<ICResMLoader> = LinkedList()
    private val loaded: MutableList<ICResMLoader> = LinkedList()

    fun <T : ICResMLoader> registerLoader(loader: T): T {
        loaders.add(loader)
        return loader
    }

    fun updateLoaders() {
        loaders.removeAll(loaded)
        loaders.forEach { loader ->
            ResourceManagerHelper.get(loader.getType()).registerReloadListener(loader)
            loaded.add(loader)
        }
    }

    fun onInitialize() {
        updateLoaders()
        LOGGER.info("Finished loading Void Lib: CResM")
    }
}