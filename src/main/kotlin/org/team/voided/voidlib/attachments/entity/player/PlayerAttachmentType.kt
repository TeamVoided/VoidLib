package org.team.voided.voidlib.attachments.entity.player

import net.minecraft.util.Identifier
import org.team.voided.voidlib.attachments.AttachmentType

class PlayerAttachmentType(
    val createAttachment: (id: Identifier) -> PlayerAttachment,
    override val defaultId: Identifier
) : AttachmentType<PlayerAttachment> {
    override fun create(id: Identifier): PlayerAttachment {
        return createAttachment(id)
    }
}
