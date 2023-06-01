package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.texture.Sprite
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent.MousePressEvent
import java.util.*

open class SwitchNode(defaultValue: Boolean, private val onSprite: Sprite, private val offSprite: Sprite): Node() {
    private var value: Boolean = defaultValue
    private val listeners: MutableList<Listener> = LinkedList()

    constructor(pos: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite): this(defaultValue, onSprite, offSprite) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite): this(defaultValue, onSprite, offSprite) {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: MousePressEvent) {
        if (isTouching(event.pos)) {
            value = !value
            listeners(value)
        }
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            if (value) {
                RenderSystem.setShaderTexture(0, onSprite.atlasId)
                DrawableHelper.drawSprite(event.drawContext.matrices, globalPos.x, globalPos.y, 0, size.x, size.y, onSprite)
            } else {
                RenderSystem.setShaderTexture(0, offSprite.atlasId)
                DrawableHelper.drawSprite(event.drawContext.matrices, globalPos.x, globalPos.y, 0, size.x, size.y, offSprite)
            }
        }
    }

    fun attachListener(listener: Listener) {
        listeners += listener
    }

    operator fun plusAssign(listener: Listener) {
        attachListener(listener)
    }

    fun value() = value
}

typealias Listener = (value: Boolean) -> Unit

private operator fun MutableList<Listener>.invoke(value: Boolean) {
    forEach { it(value) }
}
