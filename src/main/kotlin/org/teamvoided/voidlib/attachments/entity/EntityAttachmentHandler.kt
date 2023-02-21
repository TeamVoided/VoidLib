package org.teamvoided.voidlib.attachments.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.util.Identifier
import java.util.*

object EntityAttachmentHandler {
    private val list: MutableList<Pair<EntityType<*>, Pair<Identifier, EntityAttachmentType>>> = LinkedList()

    fun attach(attachment: EntityAttachmentType, type: EntityType<*>) {
        attach(attachment, attachment.defaultId, type)
    }

    fun attach(attachment: EntityAttachmentType, id: Identifier, type: EntityType<*>) {
        list.add(Pair(type, Pair(id, attachment)))
    }

    inline fun <reified T : EntityAttachment> getAttachment(id: Identifier, entity: Entity): T? {
        entity as EntityAttachmentData
        val attachment = entity.getAttachment(id)

        if (attachment is T)
            return attachment

        return null
    }

    fun getList(): LinkedList<Pair<EntityType<*>, Pair<Identifier, EntityAttachmentType>>> {
        return LinkedList(list)
    }
}