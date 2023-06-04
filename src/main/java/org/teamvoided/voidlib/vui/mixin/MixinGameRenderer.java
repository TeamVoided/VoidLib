package org.teamvoided.voidlib.vui.mixin;

import com.mojang.datafixers.util.Pair;
import kotlin.Unit;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.teamvoided.voidlib.vui.v2.shader.GlProgram;

import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "loadPrograms", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    void loadAllTheShaders(ResourceFactory factory, CallbackInfo ci, List<ShaderStage> stages, List<Pair<ShaderProgram, Consumer<ShaderProgram>>> shadersToLoad) {
        GlProgram.forEachProgram(loader -> {
            shadersToLoad.add(new Pair<>(loader.getFirst().invoke(factory), (program) -> {
                loader.getSecond().invoke(program);
            }));

            return Unit.INSTANCE;
        });
    }
}
