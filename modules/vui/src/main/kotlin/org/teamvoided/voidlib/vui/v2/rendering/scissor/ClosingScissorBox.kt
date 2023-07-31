package org.teamvoided.voidlib.vui.v2.rendering.scissor

import org.teamvoided.voidlib.core.datastructures.vector.Vec2i

class ClosingScissorBox(override var pos: Vec2i, override var size: Vec2i): ScissorBox(), AutoCloseable {
    override fun close() {
        end()
    }
}