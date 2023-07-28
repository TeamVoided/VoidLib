package org.teamvoided.voidlib.woodset.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.voidlib.woodset.sign.VoidSign;

@Mixin(SignBlockEntityRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinSignBlockEntityRenderer {
	@Unique
	protected SignBlockEntity renderedBlockEntity;

	@WrapOperation(
			method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;renderSign(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V")
	)
	@SuppressWarnings("unused")
	private void setRenderedBlockEntity(SignBlockEntityRenderer instance, MatrixStack matrices, VertexConsumerProvider verticesProvider, int light, int overlay, WoodType type, Model model, Operation<Void> original, SignBlockEntity signBlockEntity) {
		this.renderedBlockEntity = signBlockEntity;
		original.call(instance, matrices, verticesProvider, light, overlay, type, model);
		this.renderedBlockEntity = null;
	}

	@Inject(method = "getTextureId", at = @At("HEAD"), cancellable = true)
	private void getSignTextureId(CallbackInfoReturnable<SpriteIdentifier> ci) {
		if (this.renderedBlockEntity != null) {
			if (this.renderedBlockEntity.getCachedState().getBlock() instanceof VoidSign signBlock) {
				ci.setReturnValue(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, signBlock.getTexture()));
			}
		}
	}
}
