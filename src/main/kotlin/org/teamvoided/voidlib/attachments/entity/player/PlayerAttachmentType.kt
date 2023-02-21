package org.teamvoided.voidlib.attachments.entity.player

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.attachments.AttachmentType

class PlayerAttachmentType(
    val createAttachment: (id: Identifier) -> PlayerAttachment,
    override val defaultId: Identifier
) : AttachmentType<PlayerAttachment> {
    override fun create(id: Identifier): PlayerAttachment {
        return createAttachment(id)
    }
}
