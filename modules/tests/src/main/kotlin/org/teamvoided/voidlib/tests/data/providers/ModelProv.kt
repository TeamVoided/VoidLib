package org.teamvoided.voidlib.tests.data.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import org.teamvoided.voidlib.tests.Tests.BlackMill

class ModelProv(output: FabricDataOutput) : FabricModelProvider(output)  {
    override fun generateBlockStateModels(gen: BlockStateModelGenerator) { BlackMill.genModels(gen) }
    override fun generateItemModels(gen: ItemModelGenerator) {}
}