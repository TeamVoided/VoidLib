package org.teamvoided.voidlib.attachments.item

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import org.teamvoided.voidlib.attachments.Attachment

interface ItemAttachment : Attachment {
    fun tick(world: World, entity: Entity, slot: Int, selected: Boolean, stack: ItemStack)
}