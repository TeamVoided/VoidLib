package org.teamvoided.voidlib.attachments.mixin;

import kotlin.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.voidlib.attachments.entity.EntityAttachment;
import org.teamvoided.voidlib.attachments.entity.EntityAttachmentData;
import org.teamvoided.voidlib.attachments.entity.EntityAttachmentHandler;
import org.teamvoided.voidlib.attachments.entity.EntityAttachmentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityAttachmentData {
    private Map<Identifier, EntityAttachment> attachments;

    @Override
    public void setAttachment(@NotNull Identifier id, @NotNull EntityAttachmentType attachment) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.put(id, attachment.create(id));
    }

    @Override
    public void setAttachment(@NotNull EntityAttachmentType attachment) {
        setAttachment(attachment.getDefaultId(), attachment);
    }

    @Override
    public EntityAttachment getAttachment(@NotNull Identifier id) {
        if (attachments == null)
            attachments = new HashMap<>();

        return attachments.get(id);
    }

    @Override
    public void removeAttachment(@NotNull Identifier id) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.remove(id);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initAttachments(EntityType<?> type, World world, CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        List<? extends EntityType<?>> types = EntityAttachmentHandler.INSTANCE.getList().stream().map(Pair::getFirst).toList();
        if (types.contains(type)) {
            Pair<EntityType<?>, Pair<Identifier, EntityAttachmentType>> pair = EntityAttachmentHandler.INSTANCE.getList().get(types.indexOf(type));
            Identifier id = pair.getSecond().getFirst();
            EntityAttachmentType aType = pair.getSecond().getSecond();
            setAttachment(id, aType);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickAttachments(CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.values().forEach(attachment -> attachment.tick((Entity) (Object) this));
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    public void readAttachmentData(NbtCompound compound, CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.values().forEach(attachment -> attachment.read(compound));
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    public void writeAttachmentData(NbtCompound compound, CallbackInfoReturnable<NbtCompound> cir) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.values().forEach(attachment -> attachment.write(compound));
    }
}
