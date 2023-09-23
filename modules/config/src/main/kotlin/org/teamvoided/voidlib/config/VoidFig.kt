package org.teamvoided.voidlib.config

import net.minecraft.util.Identifier

interface VoidFig {
    val id: Identifier
    val side: Side
    val fileType: String

    fun serialize()
    fun deserialize()
    fun matchesRawData(other: String): Boolean
    fun matches(other: VoidFig): Boolean
}