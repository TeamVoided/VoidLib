package org.teamvoided.voidlib.attachments.entity

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.attachments.AttachmentType

class EntityAttachmentType(
    val createAttachment: (id: Identifier) -> EntityAttachment,
    override val defaultId: Identifier
) : AttachmentType<EntityAttachment> {
    override fun create(id: Identifier): EntityAttachment {
        return createAttachment(id)
    }
}