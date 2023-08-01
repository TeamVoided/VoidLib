package org.teamvoided.voidlib.vui.v2.node

import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil

open class ContainerNode(val keepProportions: Boolean = true): Node() {
    var debugBox = false

    constructor(pos: Vec2i, keepProportions: Boolean = true): this(keepProportions) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, keepProportions: Boolean = true): this(keepProportions) {
        this.pos = pos
        this.size = size
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        children().forEach { it.boundTo(this.asBoundingBox(), keepProportions) }
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        if (debugBox) Pencil.drawBorder(event.drawContext, globalPos, size, ARGB(127, 127, 127))
    }
}