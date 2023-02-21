package org.teamvoided.voidlib.attachments.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import java.util.*

object ItemAttachmentHandler {
    private val list: MutableList<Pair<Item, Pair<Identifier, ItemAttachmentType>>> = LinkedList()

    fun attach(attachment: ItemAttachmentType, item: Item) {
        attach(attachment, attachment.defaultId, item)
    }

    fun attach(attachment: ItemAttachmentType, id: Identifier, item: Item) {
        list.add(Pair(item, Pair(id, attachment)))
    }

    inline fun <reified T : ItemAttachment> getAttachment(id: Identifier, stack: ItemStack): T? {
        stack as ItemStackAttachmentData

        val attachment = stack.getAttachment(id)

        if (attachment is T)
            return attachment

        return null
    }

    fun getList(): LinkedList<Pair<Item, Pair<Identifier, ItemAttachmentType>>> {
        return LinkedList(list)
    }

    fun Item.attach(attachment: ItemAttachmentType, id: Identifier): Item {
        attach(attachment, id, this)
        return this
    }

    fun Item.attach(attachment: ItemAttachmentType): Item {
        attach(attachment, this)
        return this
    }

    fun Item.getAttachment(id: Identifier, stack: ItemStack): ItemAttachment? {
        return ItemAttachmentHandler.getAttachment(id, stack)
    }
}