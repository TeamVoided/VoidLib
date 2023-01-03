package org.team.voided.voidlib.attachments.entity.player

import net.minecraft.entity.player.PlayerEntity
import org.team.voided.voidlib.attachments.Attachment

interface PlayerAttachment: Attachment {
    val type: PlayerAttachmentType
    val respawnMethod: RespawnMethod

    fun tick(player: PlayerEntity)
}