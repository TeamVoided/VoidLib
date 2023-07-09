package org.teamvoided.voidlib.cresm

import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import org.slf4j.LoggerFactory
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.cresm.loader.ICResMLoader
import org.teamvoided.voidlib.id
import java.util.*

class CResM : LibModule(id("cresm")) {
    companion object {
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
    }

    override fun commonSetup() {
        updateLoaders()
        LOGGER.info("Finished loading VoidLib: CResM")
    }

    override fun clientSetup() {}
}