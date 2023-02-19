package org.team.voided.voidlib.dimutil

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.dimutil.network.DimSyncPacket

class DimUtil: LibModule("DimUtil") {
    override fun commonSetup() {}

    override fun clientSetup() {
        ClientPlayNetworking.registerGlobalReceiver(DimSyncPacket.id) { client, _, buf, _ ->
            DimSyncPacket(buf).handle(client)
        }
    }
}