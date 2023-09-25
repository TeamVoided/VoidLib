package org.teamvoided.voidlib.config

import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

object ConfigManager {
    @ApiStatus.Internal val serverConfigs: MutableMap<Identifier, VoidFig<*>> = HashMap()
    @ApiStatus.Internal val clientConfigs: MutableMap<Identifier, VoidFig<*>> = HashMap()
    @ApiStatus.Internal val commonConfigs: MutableMap<Identifier, VoidFig<*>> = HashMap()

    fun registerConfig(config: VoidFig<*>) {
        when (config.side) {
            Side.COMMON -> commonConfigs[config.id] = config
            Side.SERVER -> serverConfigs[config.id] = config
            Side.CLIENT -> clientConfigs[config.id] = config
        }
    }
}