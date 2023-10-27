package org.teamvoided.voidlib.woodset

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockSetType
import net.minecraft.block.Blocks
import net.minecraft.block.WoodType
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.family.BlockFamily
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.RecipeProvider.generateFamily
import net.minecraft.item.HangingSignItem
import net.minecraft.item.Item
import net.minecraft.item.SignItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.woodset.block.sign.VoidHangingSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidWallHangingSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidWallSignBlock
import org.teamvoided.voidlib.woodset.mixin.FabricTagProviderInvoker
import java.util.function.Consumer

/**
 * This is main class that is used to construct all the wood type items.
 * @param modId Identifier of your mod
 * @param name  Name of the wood type. Used in the Identifiers (eg. "oak")
 * @param settings Block settings for all the blocks in the wood type
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "unchecked_cast")
class VoidMill(private val modId: String, private val name: String, settings: FabricBlockSettings) {

    val blockSetType: BlockSetType = BlockSetTypeRegistry.registerWood(name.id)
    val woodType: WoodType = WoodTypeRegistry.register(name.id, blockSetType)
    var family: BlockFamily? = null

    var sign: VoidSignBlock? = null
    private val signTexId: Identifier = "entity/signs/$name".id
    var wallSign: VoidWallSignBlock? = null
    var signItem: SignItem? = null

    var hangingSign: VoidHangingSignBlock? = null
    private val hangingSignTexId: Identifier = "entity/signs/hanging/$name".id
    var wallHangingSign: VoidWallHangingSignBlock? = null
    var hangingSignItem: HangingSignItem? = null

    //log block & item tags

    init {
        sign = VoidSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        wallSign = VoidWallSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        signItem = SignItem(Item.Settings().maxCount(16), sign, wallSign)

        hangingSign = VoidHangingSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        wallHangingSign =
            VoidWallHangingSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        hangingSignItem = HangingSignItem(hangingSign, wallHangingSign, Item.Settings().maxCount(16))

        family = BlockFamily.Builder(Blocks.DIRT).sign(sign, wallSign).build()
    }

    /**
     * Main initializer must be called in the common setup
     */
    fun commonInit() {
        if (isSign()) {
            regBlock("${name}_sign", sign!!)
            regBlock("${name}_wall_sign", wallSign!!)
            regItem("${name}_sign", signItem!!)
        }
        if (isHangingSign()) {
            regBlock("${name}_hanging_sign", hangingSign!!)
            regBlock("${name}_wall_hanging_sign", wallHangingSign!!)
            regItem("${name}_hanging_sign", hangingSignItem!!)
        }
    }

    /**
     * Client initializer must be called in the client setup
     */
    fun clientInit() {}
    fun genRecipes(c: Consumer<RecipeJsonProvider>) {
        generateFamily(c, family)
//        offerPlanksRecipe2(c, planks, logs, 4)
//        offerBarkBlockRecipe(c, wood, log)
//        offerBarkBlockRecipe(c, strippedWood, strippedLog)
//        offerHangingSignRecipe(c, hangingSign, strippedLog)
    }

    fun genItemTags(prov: FabricTagProvider.ItemTagProvider) {
        val getTag = (prov as FabricTagProviderInvoker<Item>)

        getTag.getOrCreateTagBuilder(ItemTags.SIGNS).add(signItem)
        getTag.getOrCreateTagBuilder(ItemTags.HANGING_SIGNS).add(hangingSignItem)
    }
    fun genBlockTags(prov: FabricTagProvider.BlockTagProvider) {
        val getTag = (prov as FabricTagProviderInvoker<Block>)

        getTag.getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(sign)
        getTag.getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(wallSign)
        getTag.getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(hangingSign)
        getTag.getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(wallHangingSign)
    }

    fun genModels(gen: BlockStateModelGenerator) {
//        gen.registerLog(log).log(log).wood(wood)
//        gen.registerLog(strippedLog).log(strippedLog).wood(strippedWood)
        gen.registerCubeAllModelTexturePool(Blocks.DIRT).family(family)
        gen.registerHangingSign(Blocks.DIRT, hangingSign, wallHangingSign)
    }

    fun genLootTables(g: BlockLootTableGenerator) {
        g.addDrop(sign, signItem)
        g.addDrop(hangingSign, wallHangingSign)
//        g.add(CharredWoodSet.CHARRED_DOOR) { doorDrops(it) }

    }

    private fun isSign() = sign != null && wallSign != null
    private fun isHangingSign() = sign != null && wallSign != null
    private fun regBlock(id: String, b: Block) = Registry.register(Registries.BLOCK, id.id, b)
    private fun regItem(id: String, i: Item) = Registry.register(Registries.ITEM, id.id, i)
    private val String.id get() = Identifier(modId, this)
}