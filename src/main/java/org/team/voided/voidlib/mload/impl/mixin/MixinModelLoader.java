package org.team.voided.voidlib.mload.impl.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.team.voided.voidlib.mload.impl.ObjLoaderImpl;

@Environment(EnvType.CLIENT)
@Mixin(value = ModelLoader.class, priority = 100)
public abstract class MixinModelLoader {
    @Shadow @Final private ResourceManager resourceManager;

    @Shadow protected abstract void putModel(Identifier id, UnbakedModel unbakedModel);

    @Unique private ModelResourceProvider provider;

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void addObjModel(Identifier id, CallbackInfo info) {
        if (this.provider == null) {
            this.provider = new ObjLoaderImpl(resourceManager);
        }

        try {
            UnbakedModel model = provider.loadModelResource(id, null);

            if (model != null) {
                this.putModel(id, model);
                info.cancel();
            }
        } catch (ModelProviderException ex) {
            ex.printStackTrace();
        }
    }
}
