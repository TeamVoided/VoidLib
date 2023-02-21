package org.teamvoided.voidlib.attachments

import net.minecraft.util.Identifier

interface AttachmentData<A : Attachment, T : AttachmentType<A>> {
    fun setAttachment(id: Identifier, attachment: T)
    fun setAttachment(attachment: T)
    fun getAttachment(id: Identifier): A?
    fun removeAttachment(id: Identifier)
}