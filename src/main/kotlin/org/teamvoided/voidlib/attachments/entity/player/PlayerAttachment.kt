package org.teamvoided.voidlib.attachments.entity.player

import net.minecraft.entity.player.PlayerEntity
import org.teamvoided.voidlib.attachments.Attachment

interface PlayerAttachment : Attachment {
    val type: PlayerAttachmentType
    val respawnMethod: RespawnMethod

    fun tick(player: PlayerEntity)
}