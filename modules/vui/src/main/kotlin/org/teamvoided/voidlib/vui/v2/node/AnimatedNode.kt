package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.vui.v2.animation.Animation
import org.teamvoided.voidlib.vui.v2.animation.ListExtAny.update
import org.teamvoided.voidlib.vui.v2.event.ui.Event

class AnimatedNode(val node: Node, animations: (Node) -> List<org.teamvoided.voidlib.vui.v2.animation.Animation<*>>): Node() {
    private val animations = animations(node)

    init {
        this.pos = node.pos
        this.size = node.size
        addChild(node)
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        this.globalPos = children()[0].globalPos
        this.size = children()[0].size
        animations.update(event.updateContext.delta / 20)
    }
}