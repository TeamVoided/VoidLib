package org.teamvoided.voidlib.attachments.entity.player

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import java.util.*

object PlayerAttachmentHandler {
    private val attachments: MutableList<Pair<UUID, PlayerAttachment>> = LinkedList()

    fun attach(uuid: UUID, type: PlayerAttachmentType) {
        attach(uuid, type.defaultId, type)
    }

    fun attach(uuid: UUID, id: Identifier, type: PlayerAttachmentType) {
        attachments.add(Pair(uuid, type.create(id)))
    }

    inline fun <reified T : PlayerAttachment> getAttachment(player: PlayerEntity): T? {
        val uuids = getAttachments().stream().map { it.first }.toList()
        var attachment: PlayerAttachment? = null

        if (uuids.contains(player.uuid)) {
            val index = uuids.indexOf(player.uuid)
            attachment = getAttachments()[index].second

        }

        if (attachment is T)
            return attachment

        return null
    }

    fun removeAttachment(index: Int) {
        attachments.removeAt(index)
    }

    fun getAttachments(): List<Pair<UUID, PlayerAttachment>> {
        return LinkedList(attachments)
    }
}