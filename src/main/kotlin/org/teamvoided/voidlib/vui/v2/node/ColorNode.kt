package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.LOGGER
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.DrawHelper
import org.teamvoided.voidlib.vui.v2.event.Event

class ColorNode(var color: ARGB) : Node() {

    constructor(pos: Vec2i, color: ARGB) : this(color) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, color: ARGB) : this(color) {
        this.pos = pos
        this.size = size
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        LOGGER.info("$globalPos $size")
        LOGGER.info("color$color int-color${color.toInt()}")
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val matrices = event.drawContext.matrices

            DrawHelper.drawRect(matrices, globalPos, size, color)
        }
    }
}