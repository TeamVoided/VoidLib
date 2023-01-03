package org.team.voided.voidlib.attachments.entity

import net.minecraft.entity.Entity
import org.team.voided.voidlib.attachments.Attachment

interface EntityAttachment : Attachment {
    fun tick(entity: Entity)
}
