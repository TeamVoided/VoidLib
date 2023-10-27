package org.teamvoided.voidlib.woodset

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.family.BlockFamily
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.RecipeProvider.*
import net.minecraft.item.*
import net.minecraft.registry.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.teamvoided.voidlib.woodset.block.sign.VoidHangingSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidWallHangingSignBlock
import org.teamvoided.voidlib.woodset.block.sign.VoidWallSignBlock
import java.util.concurrent.CompletableFuture
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

    var log: PillarBlock? = null
    var strippedLog: PillarBlock? = null
    var wood: PillarBlock? = null
    var strippedWood: PillarBlock? = null

    val logBlockTag: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, "${name}_logs".id)
    val logItemTag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, "${name}_logs".id)

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
        log = PillarBlock(FabricBlockSettings.copyOf(settings))
        strippedLog = PillarBlock(FabricBlockSettings.copyOf(settings))
        wood = PillarBlock(FabricBlockSettings.copyOf(settings))
        strippedWood = PillarBlock(FabricBlockSettings.copyOf(settings))

        sign = VoidSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        wallSign = VoidWallSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        signItem = SignItem(Item.Settings().maxCount(16), sign, wallSign)

        hangingSign = VoidHangingSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        wallHangingSign =
            VoidWallHangingSignBlock(signTexId, FabricBlockSettings.copyOf(settings).noCollision(), woodType)
        hangingSignItem = HangingSignItem(hangingSign, wallHangingSign, Item.Settings().maxCount(16))

        family = BlockFamily.Builder(Blocks.DIRT)
            .sign(sign, wallSign)
            .build()
    }

    /**
     * Main initializer must be called in the common setup
     */
    @Suppress("ConstantConditionIf")
    fun commonInit() {
        if (true) {
            regBlockItem("${name}_log", log!!)
            regBlockItem("stripped_${name}_log", strippedLog!!)

            regBlockItem("${name}_wood", wood!!)
            regBlockItem("stripped_${name}_wood", strippedWood!!)

            StrippableBlockRegistry.register(log, strippedLog)
            StrippableBlockRegistry.register(wood, strippedWood)
        }

        if (true) {
            regBlock("${name}_sign", sign!!)
            regBlock("${name}_wall_sign", wallSign!!)
            regItem("${name}_sign", signItem!!)
        }
        if (true) {
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
//        offerPlanksRecipe2(c, planks, logs, 4)

        generateFamily(c, family)

        offerBarkBlockRecipe(c, wood, log)
        offerBarkBlockRecipe(c, strippedWood, strippedLog)

        offerHangingSignRecipe(c, hangingSign, strippedLog)
    }


    /**
     * To register this you must add this function as a data provider
     *<pre>{@code
     *  pack.addProvider { o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup> ->
     *      VoidMill.genBlockTags(o, r)
     *}}<pre>
     */
    fun genItemTag(
        o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ): FabricTagProvider.ItemTagProvider {
        return object : FabricTagProvider.ItemTagProvider(o, r) {
            override fun configure(arg: RegistryWrapper.WrapperLookup) {
                getOrCreateTagBuilder(logItemTag)
                    .add(log?.asItem()).add(strippedLog?.asItem())
                    .add(wood?.asItem()).add(strippedWood?.asItem())

                getOrCreateTagBuilder(ItemTags.LOGS).addTag(logItemTag)

                getOrCreateTagBuilder(ItemTags.SIGNS).add(signItem)
                getOrCreateTagBuilder(ItemTags.HANGING_SIGNS).add(hangingSignItem)
            }
        }
    }

    fun genBlockTags(
        o: FabricDataOutput, r: CompletableFuture<RegistryWrapper.WrapperLookup>,
    ): FabricTagProvider.BlockTagProvider {
        return object : FabricTagProvider.BlockTagProvider(o, r) {
            override fun configure(arg: RegistryWrapper.WrapperLookup) {
                getOrCreateTagBuilder(logBlockTag)
                    .add(log).add(strippedLog)
                    .add(wood).add(strippedWood)

                getOrCreateTagBuilder(BlockTags.LOGS).addTag(logBlockTag)

                getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(sign)
                getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(wallSign)
                getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(hangingSign)
                getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(wallHangingSign)
            }
        }
    }

    fun genModels(gen: BlockStateModelGenerator) {
        gen.registerLog(log).log(log).wood(wood)
        gen.registerLog(strippedLog).log(strippedLog).wood(strippedWood)

        gen.registerCubeAllModelTexturePool(Blocks.DIRT).family(family)

        gen.registerHangingSign(Blocks.DIRT, hangingSign, wallHangingSign)
    }

    fun genLootTables(g: BlockLootTableGenerator) {
        g.addDrop(log)
        g.addDrop(strippedLog)
        g.addDrop(wood)
        g.addDrop(strippedWood)

        g.addDrop(sign, signItem)
        g.addDrop(hangingSign, wallHangingSign)
//        g.add(CharredWoodSet.CHARRED_DOOR) { doorDrops(it) }
    }

    @ApiStatus.Experimental
    fun addAfter(itemGroup: RegistryKey<ItemGroup>, item: Item){
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(
            ItemGroupEvents.ModifyEntries {
                it.addAfter(item, log, wood, strippedLog, strippedWood)
            }
        )
    }
    private fun regBlockItem(id: String, b: Block) {
        regBlock(id, b)
        regItem(id, BlockItem(b, FabricItemSettings()))
    }

    private fun regBlock(id: String, b: Block) = Registry.register(Registries.BLOCK, id.id, b)
    private fun regItem(id: String, i: Item) = Registry.register(Registries.ITEM, id.id, i)
    private val String.id get() = Identifier(modId, this)
}