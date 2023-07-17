package org.teamvoided.voidlib.vui.impl

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.VertexFormats
import net.minecraft.resource.ResourceType
import net.minecraft.sound.SoundCategory
import org.teamvoided.voidlib.core.LOGGER
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.impl.screen.EditorScreen
import org.teamvoided.voidlib.vui.v2.animation.EasingFunction
import org.teamvoided.voidlib.vui.v2.animation.Interpolator
import org.teamvoided.voidlib.vui.v2.shader.GlProgram

object Vui {
    val hsvProgram = GlProgram(id("vui", "hsv"), VertexFormats.POSITION_COLOR)

    val openEditor = System.getProperty("vuieditor") != null
    val stopMusic = System.getProperty("vuistopmusic") != null

    fun commonSetup() {
        EasingFunction.init()
        Interpolator.init()

        ShaderProgram.SHADERS_DIRECTORY
    }

    fun clientSetup() {
        LOGGER.info("Open Editor = $openEditor")
        LOGGER.info("Stop Music = $stopMusic")

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(VuiSpriteManager)

        ClientLifecycleEvents.CLIENT_STARTED.register {
            if (openEditor) it.setScreen(EditorScreen())
            if (stopMusic) it.soundManager.updateSoundVolume(SoundCategory.MUSIC, 0f)
        }
        LOGGER.info("Finished loading Void Lib: Vui")
    }
}