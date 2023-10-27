package org.teamvoided.voidlib.tests

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import org.teamvoided.voidlib.tests.Tests.BlackMill
import org.teamvoided.voidlib.tests.Tests.LOGGER
import org.teamvoided.voidlib.tests.data.providers.LootTableProv
import org.teamvoided.voidlib.tests.data.providers.ModelProv
import org.teamvoided.voidlib.tests.data.providers.RecipeProv
import java.util.concurrent.CompletableFuture

@Suppress("unused")
class TestsData : DataGeneratorEntrypoint {
    override  fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        LOGGER.info("Test Data")
        val pack: FabricDataGenerator.Pack = gen.createPack()
        pack.addProvider { o: FabricDataOutput -> ModelProv(o) }
        pack.addProvider { o: FabricDataOutput -> LootTableProv(o) }
        pack.addProvider { o, r -> BlackMill.genBlockTags(o, r) }
        pack.addProvider { o, r -> BlackMill.genItemTag(o, r) }
        pack.addProvider { o: FabricDataOutput -> RecipeProv(o) }
    }
}