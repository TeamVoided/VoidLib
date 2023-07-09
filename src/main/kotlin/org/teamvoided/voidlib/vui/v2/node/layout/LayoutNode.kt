package org.teamvoided.voidlib.vui.v2.node.layout

import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.data.Padding
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.node.Node
import org.teamvoided.voidlib.vui.v2.rendering.scissor.ScissorStack
import org.teamvoided.voidlib.vui.v2.rendering.scissor.SimpleScissorBox

abstract class LayoutNode(): Node() {
    var padding = Padding.none
    var background: Node? = null
    var clampToSize = true

    init {
        background?.dismount()
    }

    constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        background?.globalPos = this.globalPos
        background?.size = this.size

        background?.dispatchLogicalEvent(event.updateContext)
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        val matrices = event.drawContext.matrices

        event.ensurePreChild {
            if (clampToSize) {
                ScissorStack.push(
                    SimpleScissorBox(pos + Vec2i(padding.left, padding.top), size + Vec2i(padding.horizontal, padding.vertical)),
                    matrices
                )
            } else {
                ScissorStack.pop()
            }

            background?.dispatchLogicalEvent(event.drawContext)
        }

        event.ensurePostChild {
            ScissorStack.pop()
        }
    }

    fun calculateChildSpace(): Vec2i {
        return Vec2i(size.x - padding.horizontal, size.y - padding.vertical)
    }

    fun calculateChildSpace(space: Vec2i): Vec2i {
        return Vec2i(space.x - padding.horizontal, space.y - padding.vertical)
    }
}