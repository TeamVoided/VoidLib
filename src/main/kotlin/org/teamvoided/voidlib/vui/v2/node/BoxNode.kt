package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.rendering.DrawHelper
import org.teamvoided.voidlib.vui.v2.event.Event

class BoxNode(var topLeftColor: ARGB, var topRightColor: ARGB, var bottomLeftColor: ARGB, var bottomRightColor: ARGB) : Node() {

    constructor(pos: Vec2i, topLeftColor: ARGB, topRightColor: ARGB, bottomLeftColor: ARGB, bottomRightColor: ARGB): this(topLeftColor, topRightColor, bottomLeftColor, bottomRightColor) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, topLeftColor: ARGB, topRightColor: ARGB, bottomLeftColor: ARGB, bottomRightColor: ARGB): this(topLeftColor, topRightColor, bottomLeftColor, bottomRightColor) {
        this.pos = pos
        this.size = size
    }

    constructor(pos: Vec2i, color: ARGB): this(color, color, color, color) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, color: ARGB): this(color, color, color, color) {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            DrawHelper.drawGradientRect(event.drawContext.matrices, globalPos, size, topLeftColor, topRightColor, bottomLeftColor, bottomRightColor)
        }
    }
}