package org.teamvoided.voidlib.vui

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.texture.SpriteLoader
import net.minecraft.client.texture.SpriteLoader.StitchResult
import net.minecraft.client.texture.SpriteLoader.fromAtlas
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.teamvoided.voidlib.core.id
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor


object VuiSpriteManager: SimpleSynchronousResourceReloadListener {
    private var loader: SpriteLoader? = null
    private var atlas: SpriteAtlasTexture? = null
    private var firstReload = true
    val atlasId = id("vui", "textures/atlas/vres.png")

    override fun reload(
        synchronizer: ResourceReloader.Synchronizer?,
        manager: ResourceManager,
        prepareProfiler: Profiler,
        applyProfiler: Profiler,
        prepareExecutor: Executor,
        applyExecutor: Executor
    ): CompletableFuture<Void> {
        if (atlas == null)
            atlas = SpriteAtlasTexture(atlasId)

        if (loader == null)
            loader = fromAtlas(atlas!!)

        val result = loader!!.load(manager, id("vui", "vres"), 0, prepareExecutor).thenCompose { it.whenComplete() }

        return result.thenCompose { preparedObject -> synchronizer!!.whenPrepared(preparedObject) }
            .thenAcceptAsync({ afterReload(it, applyProfiler) }, applyExecutor)
    }

    private fun afterReload(result: StitchResult, profiler: Profiler) {
        profiler.startTick()
        profiler.push("upload")
        atlas!!.upload(result)
        profiler.pop()
        profiler.endTick()

        val textureManager = MinecraftClient.getInstance().textureManager
        if (firstReload) {
            textureManager.registerTexture(atlasId, atlas!!)
            firstReload = false
        }
    }

    override fun reload(manager: ResourceManager?) { }

    override fun getFabricId(): Identifier = id("vui", "vres")

    fun atlas() = atlas

    fun getSprite(id: Identifier): Sprite? = atlas?.getSprite(id)
}