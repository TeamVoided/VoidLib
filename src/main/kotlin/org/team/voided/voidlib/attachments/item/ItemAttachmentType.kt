package org.team.voided.voidlib.attachments.item

import net.minecraft.util.Identifier
import org.team.voided.voidlib.attachments.AttachmentType

class ItemAttachmentType(val createAttachment: (id: Identifier) -> ItemAttachment, override val defaultId: Identifier) :
    AttachmentType<ItemAttachment> {
    override fun create(id: Identifier): ItemAttachment {
        return createAttachment(id)
    }
}