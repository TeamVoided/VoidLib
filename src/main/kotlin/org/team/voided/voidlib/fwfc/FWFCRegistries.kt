package org.team.voided.voidlib.fwfc

import org.team.voided.voidlib.fwfc.wave.Tile
import org.team.voided.voidlib.id
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.util.registry.Registry

class FWFCRegistries {
    companion object {
        val TILE_REGISTRY: Registry<Tile> = FabricRegistryBuilder.createSimple(Tile::class.java, id("tile_registry")).buildAndRegister()
    }
}