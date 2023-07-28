package org.teamvoided.voidlib.woodset.mixin;

//import net.minecraft.block.BlockState;
//import net.minecraft.item.VerticallyAttachableBlockItem;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.WorldView;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.teamvoided.voidlib.woodset.sign.VoidSign;

//@Mixin(VerticallyAttachableBlockItem.class)
//public class VerticallyAttachableBlockItemMixin {
//
//    @Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
//    private void canPlaceAt(WorldView worldView, BlockState blockState, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){
//        if (blockState.getBlock() instanceof VoidSign) cir.setReturnValue(true);
//    }
//}
