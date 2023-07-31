package org.teamvoided.voidlib.woodset

//import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
//import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
//import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.ModifyEntries
//import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
//import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeRegistry
//import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeRegistry
//import net.minecraft.block.Block
//import net.minecraft.block.Blocks
//import net.minecraft.client.render.TexturedRenderLayers
//import net.minecraft.client.util.SpriteIdentifier
//import net.minecraft.item.*
//import net.minecraft.registry.Registries
//import net.minecraft.registry.Registry
//import net.minecraft.util.Identifier
//import org.slf4j.LoggerFactory
//import org.teamvoided.voidlib.woodset.sign.MaterialRegistry
//import org.teamvoided.voidlib.woodset.sign.block.VoidHangingSignBlock
//import org.teamvoided.voidlib.woodset.sign.block.VoidSignBlock
//import org.teamvoided.voidlib.woodset.sign.block.VoidWallHangingSignBlock
//import org.teamvoided.voidlib.woodset.sign.block.VoidWallSignBlock

@Suppress("unused")
object WoodSet {

//    private const val MOD_ID = "voidlib"
//
//    @JvmField
//    val LOGGER = LoggerFactory.getLogger(this::class.java)
//
//    val CUSTOM_PLANKS_ID = Identifier(MOD_ID, "custom_planks")
//    private val blocks: Set<Block> = setOf()
//
//
//    private val SIGN_TEXTURE_ID = Identifier(MOD_ID, "entity/signs/custom")
//    private val HANGING_SIGN_TEXTURE_ID = Identifier(MOD_ID, "entity/signs/hanging/custom")
//    private val HANGING_SIGN_GUI_TEXTURE_ID = Identifier(MOD_ID, "textures/gui/hanging_signs/custom")
//    private val CUSTOM_SIGN_ID = Identifier(MOD_ID, "custom_sign")
//    private val CUSTOM_WALL_SIGN_ID = Identifier(MOD_ID, "custom_wall_sign")
//    private val CUSTOM_HANGING_SIGN_ID = Identifier(MOD_ID, "custom_hanging_sign")
//    private val CUSTOM_WALL_HANGING_SIGN_ID = Identifier(MOD_ID, "custom_wall_hanging_sign")
//
//
//    private val CUSTOM_BLOCK_SET_TYPE = BlockSetTypeRegistry.registerWood(Identifier(MOD_ID, "custom"))
//    private val CUSTOM_WOOD_TYPE = WoodTypeRegistry.register(Identifier(MOD_ID, "custom"), CUSTOM_BLOCK_SET_TYPE)
    fun commonSetup() {
//        LOGGER.info("Client Init")
//
//        // Signs
//        val sign: Block = VoidSignBlock(SIGN_TEXTURE_ID, FabricBlockSettings.copyOf(Blocks.OAK_SIGN), CUSTOM_WOOD_TYPE)
//        val wallSign: Block =
//            VoidWallSignBlock(SIGN_TEXTURE_ID, FabricBlockSettings.copyOf(Blocks.OAK_WALL_SIGN), CUSTOM_WOOD_TYPE)
//
//        val hangingSign: Block = VoidHangingSignBlock(
//            HANGING_SIGN_TEXTURE_ID, FabricBlockSettings.copyOf(Blocks.OAK_HANGING_SIGN), CUSTOM_WOOD_TYPE
//        )
//        val wallHangingSign: Block = VoidWallHangingSignBlock(
//            HANGING_SIGN_TEXTURE_ID, FabricBlockSettings.copyOf(Blocks.OAK_WALL_HANGING_SIGN), CUSTOM_WOOD_TYPE
//        )
//
//        val signItem: Item = SignItem(Item.Settings().maxCount(16), sign, wallSign)
//        val hangingSignItem: Item = HangingSignItem(hangingSign, wallHangingSign, Item.Settings().maxCount(16))
//
//        blocks.plus(sign)
//
//        // Register
//        Registry.register(Registries.BLOCK, CUSTOM_SIGN_ID, sign)
//        Registry.register(Registries.BLOCK, CUSTOM_WALL_SIGN_ID, wallSign)
//        Registry.register(Registries.BLOCK, CUSTOM_HANGING_SIGN_ID, hangingSign)
//        Registry.register(Registries.BLOCK, CUSTOM_WALL_HANGING_SIGN_ID, wallHangingSign)
//
//        Registry.register(Registries.ITEM, CUSTOM_SIGN_ID, signItem)
//        Registry.register(Registries.ITEM, CUSTOM_HANGING_SIGN_ID, hangingSignItem)
//
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
//            .register(ModifyEntries { entries: FabricItemGroupEntries ->
//                entries.addAfter(Items.BAMBOO_HANGING_SIGN, signItem, hangingSignItem)
//            })
    }

    fun clientSetup() {
//        LOGGER.info("Server Init")
//        MaterialRegistry.addId(
//            SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, SIGN_TEXTURE_ID)
//        )
//        MaterialRegistry.addId(
//            SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, HANGING_SIGN_TEXTURE_ID)
//        )
    }
}