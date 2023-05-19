package org.teamvoided.voidlib.attachments.mixin;

import kotlin.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.voidlib.attachments.item.ItemAttachment;
import org.teamvoided.voidlib.attachments.item.ItemAttachmentHandler;
import org.teamvoided.voidlib.attachments.item.ItemAttachmentType;
import org.teamvoided.voidlib.attachments.item.ItemStackAttachmentData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Implements(@Interface(iface = ItemStackAttachmentData.class, prefix = "isad$"))
@Mixin(ItemStack.class)
public abstract class MixinItemStack implements ItemStackAttachmentData {
    private Map<Identifier, ItemAttachment> attachments;

    @Override
    public void setAttachment(@NotNull Identifier id, @NotNull ItemAttachmentType attachment) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.put(id, attachment.create(id));
    }

    @Override
    public ItemAttachment getAttachment(@NotNull Identifier id) {
        if (attachments == null)
            attachments = new HashMap<>();

        return attachments.get(id);
    }

    @Override
    public void removeAttachment(@NotNull Identifier id) {
        if (attachments == null) {
            attachments = new HashMap<>();
            return;
        }

        attachments.remove(id);
    }

    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("RETURN"))
    private void init(ItemConvertible convertible, int count, CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        if (convertible == null) return;

        List<Item> items = ItemAttachmentHandler.INSTANCE.getList().stream().map(Pair::getFirst).toList();
        if (items.contains(convertible.asItem())) {
            Pair<Item, Pair<Identifier, ItemAttachmentType>> pair = ItemAttachmentHandler.INSTANCE.getList().get(items.indexOf(convertible.asItem()));
            Identifier id = pair.getSecond().getFirst();
            ItemAttachmentType type = pair.getSecond().getSecond();
            setAttachment(id, type);
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void write(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.values().forEach(itemAttachmentType -> itemAttachmentType.write(nbt));
    }

    @Inject(method = "setNbt", at = @At("HEAD"))
    private void read(NbtCompound nbt, CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        attachments.values().forEach(itemAttachmentType -> itemAttachmentType.read(nbt));
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void tickAttachments(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (attachments == null)
            attachments = new HashMap<>();

        for (ItemAttachment attachment : attachments.values()) {
            attachment.tick(world, entity, slot, selected, (ItemStack) (Object) this);
        }
    }
}
