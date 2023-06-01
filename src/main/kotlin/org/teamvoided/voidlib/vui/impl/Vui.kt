package org.teamvoided.voidlib.vui.impl

import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType
import org.teamvoided.voidlib.LOGGER
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.v2.animation.EasingFunction

class Vui: LibModule("vui") {
    companion object {
        val openEditor = System.getProperty("vuieditor") != null
        val stopMusic = System.getProperty("vuistopmusic") != null
    }

    override fun commonSetup() {
        EasingFunction.init()
    }

    override fun clientSetup() {
        LOGGER.info("Open Editor = $openEditor")
        LOGGER.info("Stop Music = $stopMusic")
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(VuiSpriteManager)
    }
}