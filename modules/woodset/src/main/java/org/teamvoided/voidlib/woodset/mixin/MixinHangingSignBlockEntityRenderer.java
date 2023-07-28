package org.teamvoided.voidlib.woodset.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.voidlib.woodset.sign.VoidSign;

@Mixin(HangingSignBlockEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinHangingSignBlockEntityRenderer extends MixinSignBlockEntityRenderer {
	@Inject(method = "getTextureId", at = @At("HEAD"), cancellable = true)
	private void getHangingSignTextureId(CallbackInfoReturnable<SpriteIdentifier> ci) {
		if (this.renderedBlockEntity != null) {
			if (this.renderedBlockEntity.getCachedState().getBlock() instanceof VoidSign signBlock) {
				ci.setReturnValue(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, signBlock.getTexture()));
			}
		}
	}
}
