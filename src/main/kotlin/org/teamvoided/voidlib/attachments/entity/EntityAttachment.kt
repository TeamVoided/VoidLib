package org.teamvoided.voidlib.attachments.entity

import net.minecraft.entity.Entity
import org.teamvoided.voidlib.attachments.Attachment

interface EntityAttachment : Attachment {
    fun tick(entity: Entity)
}
