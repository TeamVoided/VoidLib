package org.teamvoided.voidlib.config.impl

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import org.jetbrains.annotations.ApiStatus
import org.teamvoided.voidlib.config.ConfigManager
import org.teamvoided.voidlib.config.VoidFigHelpers
import org.teamvoided.voidlib.config.network.ConfigSyncPacket

object VoidFigImpl {
    @ApiStatus.Internal
    var saveCommonConfigsOnClient = true

    fun commonSetup() {
        ServerPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.id, ConfigSyncPacket)
        ServerLifecycleEvents.SERVER_STARTING.register {
            ConfigManager.serverConfigs.forEach { (_, config) ->
                if (!VoidFigHelpers.getConfigFile(config.id, config.side, config.fileType).exists())
                    config.serialize()
                config.deserialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                config.deserialize()
            }
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            ConfigManager.serverConfigs.forEach { (_, config) ->
                config.serialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                config.serialize()
            }
        }

        ServerPlayConnectionEvents.JOIN.register { _, sender, _ ->
            val buf = PacketByteBuf(Unpooled.buffer())
            ConfigSyncPacket.serverHandshakeInit(buf)
            sender.sendPacket(ConfigSyncPacket.id, buf)
        }
    }

    fun clientSetup() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.id, ConfigSyncPacket)
        ClientLifecycleEvents.CLIENT_STARTED.register {
            ConfigManager.clientConfigs.forEach { (_, config) ->
                if (!VoidFigHelpers.getConfigFile(config.id, config.side, config.fileType).exists())
                    config.serialize()
                config.deserialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                config.deserialize()
            }
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            ConfigManager.clientConfigs.forEach { (_, config) ->
                config.serialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                if (saveCommonConfigsOnClient) config.serialize()
            }
        }
    }
}