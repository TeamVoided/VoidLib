package org.teamvoided.voidlib.config

import net.minecraft.util.Identifier

interface VoidFig<Config> {
    val id: Identifier
    val side: Side
    val fileType: String

    var config: Config

    fun serialize()
    fun deserialize()
    fun matchesRawData(other: String): Boolean
    fun matches(other: VoidFig<Config>): Boolean
}