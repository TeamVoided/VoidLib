package org.teamvoided.voidlib.pow.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.voidlib.pow.client.ScreenEnergyBarRenderer;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreen {
    /*
    @Inject(method = "renderWithTooltip", at = @At("HEAD"))
    public void renderEnergyBar(DrawContext drawContext, int i, int j, float f, CallbackInfo ci) {
        ScreenEnergyBarRenderer.INSTANCE.renderEnergyBar((Screen) (Object) this, components, x, y);
    }
     */
}
