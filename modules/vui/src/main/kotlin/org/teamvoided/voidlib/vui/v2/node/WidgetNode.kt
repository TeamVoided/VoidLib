package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.widget.Widget
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.ui.Event

class WidgetNode(private val widgetConstructor: (pos: Vec2i, size: Vec2i) -> Widget) : Node() {
    private var widget: Widget

    init {
        this.widget = widgetConstructor(pos, size)

        resizeCallback += {
            widget = widgetConstructor(pos, size)
        }
    }

    constructor(pos: Vec2i, widget: (pos: Vec2i, size: Vec2i) -> Widget) : this(widget) {
        this.pos = pos
        this.widget = widget(pos, size)
    }

    constructor(pos: Vec2i, size: Vec2i, widget: (pos: Vec2i, size: Vec2i) -> Widget) : this(widget) {
        this.size = size
        this.widget = widget(pos, size)
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val context = event.drawContext
            if (widget is Drawable)
                (widget as Drawable).render(context.vanillaInstance, context.mousePos.x, context.mousePos.y, context.delta)
        }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (widget is Element)
            (widget as Element).mouseClicked(event.pos.x, event.pos.y, event.button)
    }

    override fun onMouseRelease(event: Event.InputEvent.MouseReleaseEvent) {
        if (widget is Element)
            (widget as Element).mouseReleased(event.pos.x, event.pos.y, event.button)
    }

    override fun onMouseDrag(event: Event.InputEvent.MouseDragEvent) {
        if (widget is Element) {
            (widget as Element).isFocused = isTouching(event.pos)
            (widget as Element).mouseDragged(event.pos.x, event.pos.y, event.button, event.delta.x, event.delta.y)
            (widget as Element).mouseMoved(event.pos.x, event.pos.y)
        }
    }

    override fun onMouseScroll(event: Event.InputEvent.MouseScrollEvent) {
        if (widget is Element)
            (widget as Element).mouseScrolled(event.pos.x, event.pos.y, event.amount)
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        if (widget is Element)
            (widget as Element).keyPressed(event.keyCode, event.scanCode, event.modifiers)
    }

    override fun onKeyRelease(event: Event.InputEvent.KeyReleaseEvent) {
        if (widget is Element)
            (widget as Element).keyReleased(event.keyCode, event.scanCode, event.modifiers)
    }

    override fun onCharTyped(event: Event.InputEvent.CharTypedEvent) {
        if (widget is Element)
            (widget as Element).charTyped(event.char, event.modifiers)
    }
}