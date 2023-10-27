package org.teamvoided.voidlib.tests.data.providers

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import org.teamvoided.voidlib.tests.Tests.BlackMill
import java.util.function.Consumer

class RecipeProv(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(gen: Consumer<RecipeJsonProvider>) { BlackMill.genRecipes(gen) }
}