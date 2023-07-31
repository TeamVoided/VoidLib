package org.teamvoided.voidlib.woodset.sign

import net.minecraft.client.util.SpriteIdentifier
import java.util.*

object MaterialRegistry {
    private val identifiers: MutableList<SpriteIdentifier>
    val ids: Collection<SpriteIdentifier> get() = Collections.unmodifiableList(identifiers)

    init {
        identifiers = ArrayList()
    }

    fun addId(sprite: SpriteIdentifier) = identifiers.add(sprite)
}