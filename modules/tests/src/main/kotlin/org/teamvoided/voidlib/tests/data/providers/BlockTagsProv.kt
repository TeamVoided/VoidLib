package org.teamvoided.voidlib.tests.data.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import org.teamvoided.voidlib.tests.Tests
import java.util.concurrent.CompletableFuture

class BlockTagsProv(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        Tests.BlackMill.genBlockTags(this)
    }
}