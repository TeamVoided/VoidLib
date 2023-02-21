package org.teamvoided.voidlib.attachments.item

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.attachments.AttachmentType

class ItemAttachmentType(val createAttachment: (id: Identifier) -> ItemAttachment, override val defaultId: Identifier) :
    AttachmentType<ItemAttachment> {
    override fun create(id: Identifier): ItemAttachment {
        return createAttachment(id)
    }
}