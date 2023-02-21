package org.teamvoided.voidlib.dimutil

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.dimutil.network.DimSyncPacket

class DimUtil: LibModule("DimUtil") {
    override fun commonSetup() {}

    override fun clientSetup() {
        ClientPlayNetworking.registerGlobalReceiver(DimSyncPacket.id) { client, _, buf, _ ->
            DimSyncPacket(buf).handle(client)
        }
    }
}