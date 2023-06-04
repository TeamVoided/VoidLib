package org.teamvoided.voidlib.vui.v2.event.fabric

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Window

fun interface WindowResizeCallback {
    companion object {
        @JvmStatic val event = EventFactory.createArrayBacked(WindowResizeCallback::class.java) { callbacks ->
            WindowResizeCallback { client, window ->
                for (callback in callbacks)
                    callback(client, window)
            }
        }
    }

    operator fun invoke(client: MinecraftClient, window: Window)
}