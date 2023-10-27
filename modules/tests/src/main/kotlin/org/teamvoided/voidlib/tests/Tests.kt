package org.teamvoided.voidlib.tests

import org.slf4j.LoggerFactory
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Blocks
import net.minecraft.item.*
import org.teamvoided.voidlib.woodset.VoidMill

@Suppress("unused")
object Tests {
    private const val TESTID = "void_test"
    val LOGGER = LoggerFactory.getLogger(Tests::class.java)
    val BlackMill = VoidMill(TESTID, "black", FabricBlockSettings.copyOf(Blocks.OAK_PLANKS))

    fun commonSetup() {
        LOGGER.info("Common")
        BlackMill.commonInit()
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(
            ModifyEntries { it.addAfter(Items.BAMBOO_HANGING_SIGN, BlackMill.signItem, BlackMill.hangingSign) }
        )
    }

    fun clientSetup() {
        LOGGER.info("Client")
        BlackMill.clientInit()
    }
}