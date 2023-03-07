package org.teamvoided.voidlib.vui.node

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.InputEvent
import org.teamvoided.voidlib.vui.node.NodeIds.MOVABLE

open class MovableNode(val node: Node, override var name: String): Node() {
    protected var selected = false
    protected var offset = Vec2i(0, 0)

    override var onMousePress: ((InputEvent.MousePressEvent) -> Unit)? = {
        if (isTouching(it.pos)) {
            selected = true
            offset = (it.pos - this.globalPos)
        }
    }

    override var onMouseRelease: ((InputEvent.MouseReleaseEvent) -> Unit)? = {
        if (selected)
            selected = false
    }

    override var onMouseDrag: ((InputEvent.MouseDragEvent) -> Unit)? = {
        if (selected) {
            val nPos = (it.pos - offset + it.delta)
            this.globalPos = nPos.copy()
            node.globalPos = nPos.copy()
        }
    }

    init {
        this.size = node.size.copy()
        this.globalPos = node.globalPos.copy()

        addChild(node)
    }

    override fun typeId(): Identifier = MOVABLE
}