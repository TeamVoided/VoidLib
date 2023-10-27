package org.teamvoided.voidlib.woodset.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.voidlib.woodset.block.sign.VoidSign;

@Debug(export = true)
@Mixin(BlockEntityType.class)
public class MixinBlockEntityType {
	@Inject(method = "supports", at = @At("HEAD"), cancellable = true)
	private void voidlib$supports(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() instanceof VoidSign) cir.setReturnValue(true);
	}
}
