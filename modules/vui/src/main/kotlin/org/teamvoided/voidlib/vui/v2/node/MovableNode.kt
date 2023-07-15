package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent.*


open class MovableNode(val node: Node): Node() {
    protected var selected = false
    protected var offset = Vec2i(0, 0)

    init {
        this.size = node.size.copy()
        this.globalPos = node.globalPos.copy()

        addChild(node)
    }

    override fun onMousePress(event: MousePressEvent) {
        if (isTouching(event.pos)) {
            selected = true
            offset = (event.pos.to2i() - this.globalPos)
            event.cancel()
        }
    }

    override fun onMouseRelease(event: MouseReleaseEvent) {
        if (selected) {
            selected = false
            event.cancel()
        }
    }

    override fun onMouseDrag(event: MouseDragEvent) {
        if (selected) {
            val nPos = (event.pos.to2i() - offset + event.delta.to2i())
            this.globalPos = nPos
            node.globalPos = nPos
            event.cancel()
        }
    }
}