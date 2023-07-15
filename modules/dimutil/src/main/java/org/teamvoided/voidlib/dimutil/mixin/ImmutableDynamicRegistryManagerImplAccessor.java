package org.teamvoided.voidlib.dimutil.mixin;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DynamicRegistryManager.ImmutableImpl.class)
public interface ImmutableDynamicRegistryManagerImplAccessor {
    @Accessor("registries")
    Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> getRegistries();

    @Accessor("registries")
    void setRegistries(Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> registries);
}
