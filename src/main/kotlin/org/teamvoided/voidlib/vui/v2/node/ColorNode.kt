package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.gui.DrawableHelper
import org.teamvoided.voidlib.LOGGER
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event

class ColorNode(var color: RGB) : Node() {

    constructor(pos: Vec2i, color: RGB) : this(color) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, color: RGB) : this(color) {
        this.pos = pos
        this.size = size
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        LOGGER.info("$globalPos $size")
    }
    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val matrices = event.drawContext.matrices

            matrices.push()

            DrawableHelper.fill(
                matrices,
                globalPos.x,
                globalPos.y,
                globalPos.x + size.x,
                globalPos.y + size.y,
                color.toInt()
            )

            matrices.pop()
        }
    }
}