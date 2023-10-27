package org.teamvoided.voidlib.tests

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import org.teamvoided.voidlib.tests.Tests.LOGGER
import org.teamvoided.voidlib.tests.data.providers.*
import java.util.concurrent.CompletableFuture

class TestsData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        LOGGER.info("Hello from DataInit")
        val pack: FabricDataGenerator.Pack = gen.createPack()
        pack.addProvider { o: FabricDataOutput -> RecipeProv(o) }
        pack.addProvider { o: FabricDataOutput -> ModelProv(o) }
        pack.addProvider { o: FabricDataOutput -> LootTableProv(o) }
        pack.addProvider { o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup> ->
            BlockTagsProv(o, r)
        }
        pack.addProvider { o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup> ->
            ItemTagsProv(o, r)
        }
    }
}