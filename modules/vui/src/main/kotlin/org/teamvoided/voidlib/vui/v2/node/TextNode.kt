package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil

open class TextNode(var text: Text, var textColor: ARGB): Node() {
    constructor(pos: Vec2i, text: Text, textColor: ARGB): this(text, textColor) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, text: Text, textColor: ARGB): this(text, textColor) {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        Pencil.drawText(event.drawContext, text, globalPos, size.y.f, textColor)
    }
}