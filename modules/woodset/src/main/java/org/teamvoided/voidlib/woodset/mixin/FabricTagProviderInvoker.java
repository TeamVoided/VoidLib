package org.teamvoided.voidlib.woodset.mixin;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FabricTagProvider.class)
public interface FabricTagProviderInvoker<T> {

	@Invoker(value = "getOrCreateTagBuilder", remap = false)
	FabricTagProvider<T>.FabricTagBuilder getOrCreateTagBuilder(TagKey<T> tag);

}
