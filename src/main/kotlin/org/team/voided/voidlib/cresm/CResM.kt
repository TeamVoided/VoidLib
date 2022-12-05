package org.team.voided.voidlib.cresm

import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import org.slf4j.LoggerFactory
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.cresm.loader.ICResMLoader
import java.util.*

class CResM: LibModule("Custom Resource Manager") {
    companion object {
        val LOGGER = LoggerFactory.getLogger("VoidLib: CResM")

        private val loaders: MutableList<ICResMLoader> = LinkedList()
        private val loaded: MutableList<ICResMLoader> = LinkedList()

        fun registerLoader(loader: ICResMLoader): ICResMLoader {
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

    override fun clientSetup() { }
}