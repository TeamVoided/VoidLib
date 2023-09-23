package org.teamvoided.voidlib.config.impl

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.teamvoided.voidlib.config.ConfigManager
import org.teamvoided.voidlib.config.network.ConfigSyncPacket

object VoidFigImpl {
    fun commonSetup() {
        ServerPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.id, ConfigSyncPacket)
        ServerLifecycleEvents.SERVER_STARTING.register {
            ConfigManager.serverConfigs.forEach { (_, config) ->
                config.deserialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                config.deserialize()
            }
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            ConfigManager.clientConfigs.forEach { (_, config) ->
                config.serialize()
            }

            ConfigManager.commonConfigs.forEach { (_, config) ->
                config.serialize()
            }
        }
    }

    fun clientSetup() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.id, ConfigSyncPacket)
        ClientLifecycleEvents.CLIENT_STARTED.register {
            ConfigManager.clientConfigs.forEach { (_, config) ->
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
                config.serialize()
            }
        }
    }
}