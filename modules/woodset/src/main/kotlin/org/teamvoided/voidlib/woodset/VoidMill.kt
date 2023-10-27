package org.teamvoided.voidlib.woodset

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.*
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.client.render.RenderLayer
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
import org.teamvoided.voidlib.woodset.block.sign.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

/**
 * This is main class that is used to construct all the wood type items.
 * @param modId Identifier of your mod
 * @param name  Name of the wood type. Used in the Identifiers (eg. "oak")
 * @param settings Block settings for all the blocks in the wood type
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class VoidMill(private val modId: String, private val name: String, settings: FabricBlockSettings) {

    val blockSetType: BlockSetType = BlockSetTypeRegistry.registerWood(name.id)
    val woodType: WoodType = WoodTypeRegistry.register(name.id, blockSetType)
    val family: BlockFamily

    val planks: Block

    var stairs: StairsBlock? = null
    var slab: SlabBlock? = null


    var fence: FenceBlock? = null
    var fenceGate: FenceGateBlock? = null

    var pressurePlate: PressurePlateBlock? = null
    var button: ButtonBlock? = null

    var door: DoorBlock? = null
    var trapdoor: TrapdoorBlock? = null


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
        planks = Block(settings.copy)
        var familyBuilder = BlockFamily.Builder(planks)

        stairs = StairsBlock(planks.defaultState, settings.copy)
        familyBuilder = familyBuilder.stairs(stairs)
        slab = SlabBlock(settings.copy)
        familyBuilder = familyBuilder.slab(slab)

        fence = FenceBlock(settings.copy)
        familyBuilder = familyBuilder.fence(fence)
        fenceGate = FenceGateBlock(settings.copy, woodType)
        familyBuilder = familyBuilder.fenceGate(fenceGate)

        pressurePlate = PressurePlateBlock(
            PressurePlateBlock.ActivationRule.EVERYTHING, settings.copy.pistonBehavior(PistonBehavior.DESTROY),
            blockSetType
        )
        familyBuilder = familyBuilder.pressurePlate(pressurePlate)
        button = ButtonBlock(settings.copy.pistonBehavior(PistonBehavior.DESTROY), blockSetType, 30, true)
        familyBuilder = familyBuilder.button(button)

        door = DoorBlock(settings.copy.nonOpaque(), blockSetType)
        familyBuilder = familyBuilder.door(door)
        trapdoor = TrapdoorBlock(settings.copy.nonOpaque(), blockSetType)
        familyBuilder = familyBuilder.trapdoor(trapdoor)

        log = PillarBlock(settings.copy)
        strippedLog = PillarBlock(settings.copy)
        wood = PillarBlock(settings.copy)
        strippedWood = PillarBlock(settings.copy)

        sign = VoidSignBlock(signTexId, settings.copy.noCollision(), woodType)
        wallSign = VoidWallSignBlock(signTexId, settings.copy.noCollision(), woodType)
        signItem = SignItem(Item.Settings().maxCount(16), sign, wallSign)
        familyBuilder = familyBuilder.sign(sign, wallSign)

        hangingSign = VoidHangingSignBlock(signTexId, settings.copy.noCollision(), woodType)
        wallHangingSign =
            VoidWallHangingSignBlock(signTexId, settings.copy.noCollision(), woodType)
        hangingSignItem = HangingSignItem(hangingSign, wallHangingSign, Item.Settings().maxCount(16))

        family = familyBuilder.build()
    }

    /**
     * Main initializer must be called in the common setup
     */
    @Suppress("ConstantConditionIf")
    fun commonInit() {
        regBlockItem("${name}_planks", planks)

        if (true) regBlockItem("${name}_fence", fence!!)
        if (true) regBlockItem("${name}_fence_gate", fenceGate!!)

        if (true) regBlockItem("${name}_pressure_plate", pressurePlate!!)
        if (true) regBlockItem("${name}_button", button!!)

        if (true) regBlockItem("${name}_door", door!!)
        if (true) regBlockItem("${name}_trapdoor", trapdoor!!)

        if (true) regBlockItem("${name}_stairs", stairs!!)
        if (true) regBlockItem("${name}_slab", slab!!)

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
    fun clientInit() {
        if (true) BlockRenderLayerMap.INSTANCE.putBlock(door, RenderLayer.getCutout())
        if (true) BlockRenderLayerMap.INSTANCE.putBlock(trapdoor, RenderLayer.getCutout())

    }

    fun genRecipes(c: Consumer<RecipeJsonProvider>) {
        offerPlanksRecipe2(c, planks, logItemTag, 4)

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
                getOrCreateTagBuilder(ItemTags.PLANKS).add(planks.asItem())

                getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(stairs?.asItem())
                getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(slab?.asItem())

                getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(fence?.asItem())
                getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(fenceGate?.asItem())

                getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(pressurePlate?.asItem())
                getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(button?.asItem())

                getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(door?.asItem())
                getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(trapdoor?.asItem())

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
                getOrCreateTagBuilder(BlockTags.PLANKS).add(planks)

                getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(fence)
                getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(fenceGate)

                getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(pressurePlate)
                getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(button)

                getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(door)
                getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(trapdoor)

                getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(stairs)
                getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(slab)

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
        gen.registerCubeAllModelTexturePool(planks).family(family)

        gen.registerLog(log).log(log).wood(wood)
        gen.registerLog(strippedLog).log(strippedLog).wood(strippedWood)

        gen.registerHangingSign(planks, hangingSign, wallHangingSign)
    }

    fun genLootTables(g: BlockLootTableGenerator) {
        g.addDrop(planks)

        g.addDrop(fence)
        g.addDrop(fenceGate)

        g.addDrop(pressurePlate)
        g.addDrop(button)

        g.addDrop(door) { b -> g.doorDrops(b) }
        g.addDrop(trapdoor)

        g.addDrop(stairs)
        g.addDrop(slab)

        g.addDrop(log)
        g.addDrop(strippedLog)
        g.addDrop(wood)
        g.addDrop(strippedWood)

        g.addDrop(sign, signItem)
        g.addDrop(hangingSign, wallHangingSign)
    }

    /**
     * Adds all the block (Minus the signs) to a specified creative tab, after the specified item
     *
     * @param itemGroup The tab where the items get added to
     * @param item The item after which the VoidMill items will be added
     */
    @ApiStatus.Experimental
    fun addAfter(itemGroup: RegistryKey<ItemGroup>, item: Item) {
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(
            ItemGroupEvents.ModifyEntries {
                it.addAfter(
                    item, log, wood, strippedLog, strippedWood, planks, stairs, slab, fence, fenceGate, door, trapdoor,
                    pressurePlate, button
                )
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
    private val FabricBlockSettings.copy get() = FabricBlockSettings.copyOf(this)

}