package org.teamvoided.voidlib.dimutil

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import org.teamvoided.voidlib.core.LOGGER
import org.teamvoided.voidlib.dimutil.network.DimSyncPacket

object DimUtil {
    fun onInitialize() {
        ClientPlayNetworking.registerGlobalReceiver(DimSyncPacket.id) { client, _, buf, _ ->
            DimSyncPacket(buf).handle(client)
            LOGGER.info("Finished loading Void Lib: DimUtil")
        }
    }
}