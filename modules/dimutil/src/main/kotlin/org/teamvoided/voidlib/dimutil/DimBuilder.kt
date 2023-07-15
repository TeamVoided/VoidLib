package org.teamvoided.voidlib.dimutil

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.SimpleRegistry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.random.RandomSequencesState
import net.minecraft.world.World
import net.minecraft.world.biome.source.BiomeAccess
import net.minecraft.world.border.WorldBorderListener.WorldBorderSyncer
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.level.UnmodifiableLevelProperties
import org.teamvoided.voidlib.dimutil.mixin.ImmutableDynamicRegistryManagerImplAccessor
import org.teamvoided.voidlib.dimutil.mixin.MinecraftServerAccessor
import org.teamvoided.voidlib.dimutil.network.DimSyncPacket

object DimBuilder {
    fun getOrCreateWorld(server: MinecraftServer, worldKey: RegistryKey<World>, dimFactory: (server: MinecraftServer, worldKey: RegistryKey<DimensionOptions>) -> DimensionOptions): ServerWorld {
        val worlds = (server as MinecraftServerAccessor).worldMap
        val existing = worlds[worldKey]
        if (existing != null)
            return existing

        return internalGetOrCreateWorld(server, worlds, worldKey, dimFactory)
    }

    private fun internalGetOrCreateWorld(
        server: MinecraftServer,
        worlds: MutableMap<RegistryKey<World>, ServerWorld>,
        worldKey: RegistryKey<World>,
        dimFactory: (server: MinecraftServer, worldKey: RegistryKey<DimensionOptions>) -> DimensionOptions
    ): ServerWorld {
        val accessor = server as MinecraftServerAccessor
        val overworld = server.getWorld(World.OVERWORLD) ?: throw IllegalStateException("Overworld cannot be null")
        val dimKey = RegistryKey.of(RegistryKeys.DIMENSION, worldKey.value)
        val dimOps = dimFactory(server, dimKey)
        val chunkProgressListener = accessor.progressListenerFactory.create(11)
        val executor = accessor.executor
        val anvilConverter = server.session
        val saveProperties = server.saveProperties
        val genOps = saveProperties.generatorOptions
        val worldProperties = UnmodifiableLevelProperties(saveProperties, saveProperties.mainWorldProperties)
        val registries = server.combinedDynamicRegistries
        val composite = registries.combinedRegistryManager
        val regMap = HashMap((composite as ImmutableDynamicRegistryManagerImplAccessor).registries)
        val key: RegistryKey<out Registry<*>> = RegistryKey.of(RegistryKey.ofRegistry(Identifier("root")), Identifier("dimension"))
        val oldRegistry = regMap[key] as SimpleRegistry<DimensionOptions>
        val oldLifecycle = oldRegistry.lifecycle
        val newRegistry = SimpleRegistry(RegistryKeys.DIMENSION, oldLifecycle, false)
        oldRegistry.entrySet.forEach { (oldKey, dim) ->
            val oldLevelKey = RegistryKey.of(RegistryKeys.WORLD, oldKey.value)
            if (dim != null && oldLevelKey != worldKey)
                Registry.register(newRegistry, oldKey, dim)
        }
        Registry.register(newRegistry, dimKey, dimOps)
        regMap.replace(key, newRegistry)
        val newMap = regMap
        (composite as ImmutableDynamicRegistryManagerImplAccessor).registries = newMap
        val newWorld = ServerWorld(
            server,
            executor,
            anvilConverter,
            worldProperties,
            worldKey,
            dimOps,
            chunkProgressListener,
            false,
            BiomeAccess.hashSeed(genOps.seed),
            ImmutableList.of(),
            false,
            RandomSequencesState(server.overworld.seed)
        )

        overworld.worldBorder.addListener(WorldBorderSyncer(newWorld.worldBorder))
        worlds[worldKey] = newWorld

        server.playerManager.playerList.forEach {
            val packet = DimSyncPacket(ImmutableSet.of(worldKey), ImmutableSet.of())
            val buf = PacketByteBufs.create()
            packet.toBytes(buf)
            ServerPlayNetworking.send(it, DimSyncPacket.id, buf)
        }

        return newWorld
    }
}