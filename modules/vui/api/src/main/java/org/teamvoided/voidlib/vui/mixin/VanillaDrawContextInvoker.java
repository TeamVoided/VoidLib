package org.teamvoided.voidlib.vui.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(DrawContext.class)
public interface VanillaDrawContextInvoker {
    @Invoker("drawTexture") void void_drawTexture(Identifier texture, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight);
    @Invoker("drawTexturedQuad") void void_drawTexturedQuad(Identifier texture, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, float red, float green, float blue, float alpha);
    @Invoker("drawTooltip") void void_drawTooltip(TextRenderer textRenderer, List<TooltipComponent> list, int i, int j, TooltipPositioner tooltipPositioner);
    @Invoker("drawItem") void void_drawItem(@Nullable LivingEntity livingEntity, @Nullable World world, ItemStack itemStack, int i, int j, int k, int l);
}
