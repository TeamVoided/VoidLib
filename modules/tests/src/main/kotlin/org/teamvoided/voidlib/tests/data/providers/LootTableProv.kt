package org.teamvoided.voidlib.tests.data.providers
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import org.teamvoided.voidlib.tests.Tests.BlackMill
class LootTableProv(output: FabricDataOutput) : FabricBlockLootTableProvider(output) {
    override fun generate() {BlackMill.genLootTables(this) }
}