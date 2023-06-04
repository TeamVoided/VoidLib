package org.teamvoided.voidlib.vui.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenInvoker {
    @Invoker("renderTooltipFromComponents")
    void void_renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner);
}
