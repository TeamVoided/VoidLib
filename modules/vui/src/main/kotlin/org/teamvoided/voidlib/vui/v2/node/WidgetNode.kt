package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.gui.widget.ClickableWidget
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.ui.Event

class WidgetNode(var widget : ClickableWidget) : Node() {
    constructor(pos: Vec2i, widget : ClickableWidget) : this(widget) {
        this.pos = pos
        widget.x = pos.x
        widget.y = pos.y
    }

    constructor(pos: Vec2i, size: Vec2i, widget : ClickableWidget) : this(pos, widget) {
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val context = event.drawContext
            widget.render(context.vanillaInstance, context.mousePos.x, context.mousePos.y, context.delta)
        }
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild {  }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
         widget.mouseClicked(event.pos.x, event.pos.y, event.button)
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        widget.keyPressed(event.keyCode, event.scanCode, event.modifiers)

    }

}