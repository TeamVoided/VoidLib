package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.texture.Sprite
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent.MousePressEvent
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle

open class CheckboxNode(defaultValue: Boolean, private val onSprite: Sprite, private val offSprite: Sprite): Node() {
    private var value: Boolean = defaultValue
    private var hovered = false

    private val checkboxUpdateCallback: Callback<Boolean>
        get() = getCallbackAs(id("checkbox_update_callback"))

    constructor(pos: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite): this(defaultValue, onSprite, offSprite) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, defaultValue: Boolean, onSprite: Sprite, offSprite: Sprite): this(defaultValue, onSprite, offSprite) {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: MousePressEvent) {
        if (hovered) {
            value = !value
            checkboxUpdateCallback(value)
            event.cancel()
        }
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild {
            val parent = parent()
            hovered = isTouching(event.updateContext.mousePos) && ((parent != null && parent.childAt(event.updateContext.mousePos) == this) || (parent == null))
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

    override fun cursorStyle(): CursorStyle {
        return if (hovered) CursorStyle.HAND else CursorStyle.POINTER
    }

    fun value() = value
}
