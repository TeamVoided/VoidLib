package org.teamvoided.voidlib.vui.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.voidlib.vui.v2.extension.EntityRendererDispatcherExt;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRendererDispatcher implements EntityRendererDispatcherExt {
    @Shadow public Camera camera;

    private boolean void_showNametag = true;
    private boolean void_counterRotate = false;

    @Override
    public void setVoid_showNametag(boolean b) {
        void_showNametag = b;
    }

    @Override
    public boolean getVoid_showNametag() {
        return void_showNametag;
    }

    @Override
    public void setVoid_counterRotate(boolean b) {
        void_counterRotate = b;
    }

    @Override
    public boolean getVoid_counterRotate() {
        return void_counterRotate;
    }

    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", shift = At.Shift.AFTER))
    private void counterRotate(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
        if (!this.void_counterRotate) return;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.camera.getYaw() + 170));
        matrices.translate(0, 0, .1);
    }
}
