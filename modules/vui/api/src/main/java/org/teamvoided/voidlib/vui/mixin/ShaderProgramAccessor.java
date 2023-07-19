package org.teamvoided.voidlib.vui.mixin;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShaderProgram.class)
public interface ShaderProgramAccessor {

    @Accessor("loadedUniforms")
    Map<String, GlUniform> void_getLoadedUniforms();

}
