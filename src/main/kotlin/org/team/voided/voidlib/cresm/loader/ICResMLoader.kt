package org.team.voided.voidlib.cresm.loader

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceType

interface ICResMLoader : SimpleSynchronousResourceReloadListener {
    fun getType(): ResourceType
}