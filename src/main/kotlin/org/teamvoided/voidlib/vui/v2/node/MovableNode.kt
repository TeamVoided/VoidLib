package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent.*


open class MovableNode(val node: Node): Node() {
    protected var selected = false
    protected var offset = Vec2i(0, 0)

    init {
        this.size = node.size.copy()
        this.globalPos = node.globalPos.copy()

        addChild(node)
    }

    override fun onMousePress(event: MousePressEvent): Boolean {
        if (isTouching(event.pos)) {
            selected = true
            offset = (event.pos - this.globalPos)

            return false
        }

        return true
    }

    override fun onMouseRelease(event: MouseReleaseEvent): Boolean {
        if (selected) selected = false
        return true
    }

    override fun onMouseDrag(event: MouseDragEvent): Boolean {
        if (selected) {
            val nPos = (event.pos - offset + event.delta)
            this.globalPos = nPos.copy()
            node.globalPos = nPos.copy()

            return false
        }

        return true
    }
}