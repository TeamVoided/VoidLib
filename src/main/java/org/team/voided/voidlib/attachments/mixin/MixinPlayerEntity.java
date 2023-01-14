package org.team.voided.voidlib.attachments.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.team.voided.voidlib.attachments.entity.player.PlayerAttachment;
import org.team.voided.voidlib.attachments.entity.player.PlayerAttachmentHandler;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeAttachments(NbtCompound compound, CallbackInfo ci) {
        PlayerAttachmentHandler.INSTANCE.getAttachments().stream().filter((attachment) -> attachment.getFirst() == uuid).toList().forEach((pair) -> pair.getSecond().write(compound));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readAttachments(NbtCompound compound, CallbackInfo ci) {
        PlayerAttachmentHandler.INSTANCE.getAttachments().stream().filter((attachment) -> attachment.getFirst() == uuid).toList().forEach((pair) -> pair.getSecond().read(compound));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickAttachments(CallbackInfo ci) {
        PlayerAttachmentHandler.INSTANCE.getAttachments().stream().filter((attachment) -> attachment.getFirst() == uuid).toList().forEach((pair) -> pair.getSecond().tick((PlayerEntity) (Object) this));
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void calculateRespawn(DamageSource damageSource, CallbackInfo ci) {
        PlayerAttachmentHandler.INSTANCE.getAttachments().stream().filter((attachment) -> attachment.getFirst() == uuid).toList().forEach((pair) -> {
            PlayerAttachment attachment = pair.getSecond();
            int index = PlayerAttachmentHandler.INSTANCE.getAttachments().indexOf(pair);
            switch (attachment.getRespawnMethod()) {
                case DELETE_ON_RESPAWN -> PlayerAttachmentHandler.INSTANCE.removeAttachment(index);
                case KEEP_ON_RESPAWN -> {}
                case RESET_ON_RESPAWN -> {
                    PlayerAttachmentHandler.INSTANCE.removeAttachment(index);
                    PlayerAttachmentHandler.INSTANCE.attach(uuid, attachment.getId(), attachment.getType());
                }
            }
        });
    }
}
