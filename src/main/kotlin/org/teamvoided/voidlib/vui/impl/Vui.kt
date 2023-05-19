package org.teamvoided.voidlib.vui.impl

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType
import org.teamvoided.voidlib.LOGGER
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.vui.VuiSpriteManager

class Vui: LibModule("vui") {
    companion object {
        val openEditor = System.getProperty("vuieditor") != null
        val stopMusic = System.getProperty("vuistopmusic") != null
    }

    override fun commonSetup() {}

    override fun clientSetup() {
        LOGGER.info("Open Editor = $openEditor")
        LOGGER.info("Stop Music = $stopMusic")
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(VuiSpriteManager)
        ClientLifecycleEvents.CLIENT_STARTED.register {
            LOGGER.info("${it.resourceManager.allNamespaces}")
            //it.setScreen(EditorScreen(Text.literal("Editor")))
        }
    }
}