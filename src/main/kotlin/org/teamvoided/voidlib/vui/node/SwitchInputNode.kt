package org.teamvoided.voidlib.vui.node

import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.InputEvent
import org.teamvoided.voidlib.vui.node.NodeIds.SWITCH_INPUT

class SwitchInputNode(defaultValue: Boolean, val onSprite: Sprite, val offSprite: Sprite, override var name: String): Node() {
    var value = defaultValue

    override var onMousePress: ((InputEvent.MousePressEvent) -> Unit)? = {
        if (isTouching(it.pos))
            value = !value
    }

    override var drawCallback: ((context: DrawContext) -> Unit)? = {
        if (value) {
            DrawableHelper.drawSprite(it.matrices, globalPos.x, globalPos.y, 0, size.x, size.y, onSprite)
        } else {
            DrawableHelper.drawSprite(it.matrices, globalPos.x, globalPos.y, 0, size.x, size.y, offSprite)
        }
    }

    constructor(pos: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite, name: String): this(defaultValue, onSprite, offSprite, name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite, name: String): this(defaultValue, onSprite, offSprite, name) {
        this.pos = pos
        this.size = size
    }

    override fun typeId(): Identifier = SWITCH_INPUT
}