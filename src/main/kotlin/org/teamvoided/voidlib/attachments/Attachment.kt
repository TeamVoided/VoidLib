package org.teamvoided.voidlib.attachments

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier

interface Attachment {
    val id: Identifier

    fun read(compound: NbtCompound)
    fun write(compound: NbtCompound)
    override fun equals(other: Any?): Boolean
}

interface AttachmentType<T : Attachment> {
    val defaultId: Identifier

    fun create(id: Identifier): T
}
