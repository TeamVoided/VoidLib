package org.teamvoided.voidlib.vui.v2.node.layout

import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.vui.v2.data.HorizontalAlignment
import org.teamvoided.voidlib.vui.v2.data.VerticalAlignment
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.node.Node
import org.teamvoided.voidlib.vui.v2.node.layout.FlowLayout.Algorithm

open class FlowLayout(): LayoutNode() {
    var verticalAlignment = VerticalAlignment.TOP
    var horizontalAlignment = HorizontalAlignment.LEFT
    var algorithm = Algorithm.HORIZONTAL

    constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        algorithm(this)
    }

    @Suppress("duplicates")
    fun interface Algorithm {
        companion object {
            val HORIZONTAL = Algorithm { node: FlowLayout ->
                var layoutWidth = 0
                var layoutHeight = 0
                val layout = ArrayList<Node>()
                val padding = node.padding
                val childSpace = node.calculateChildSpace()

                node.children().forEach { child: Node ->
                    child.inflate(childSpace)
                }

                node.children().forEach { child ->
                    layout.add(child)
                    child.pos = Vec2i(node.pos.x + padding.left + layoutWidth, node.pos.y + padding.top)
                    val childSize = child.size
                    layoutWidth += childSize.x
                    if (childSize.y > layoutHeight) {
                        layoutHeight = childSize.y
                    }
                }

                if (node.verticalAlignment != VerticalAlignment.TOP) {
                    layout.forEach {
                        it.pos.y += node.verticalAlignment.align(it.size.y, node.size.y - padding.vertical)
                    }

                }

                if (node.horizontalAlignment != HorizontalAlignment.LEFT) {
                    layout.forEach {
                        if (node.horizontalAlignment == HorizontalAlignment.CENTER) {
                            it.pos.x = (node.size.x - padding.horizontal - layoutWidth) / 2
                        } else {
                            it.pos.x = node.size.x - padding.horizontal - layoutWidth
                        }
                    }
                }
            }

            val VERTICAL = Algorithm { node: FlowLayout ->
                var layoutHeight = 0
                var layoutWidth = 0
                val layout = ArrayList<Node>()
                val padding = node.padding
                val childSpace = node.calculateChildSpace()
                node.children().forEach { child ->
                    child.inflate(childSpace)
                }

                node.children().forEach { child ->
                    layout.add(child)
                    child.pos = Vec2i(node.pos.x + padding.left, node.pos.y + padding.top + layoutHeight.i)
                    val childSize = child.size
                    layoutHeight += childSize.y
                    if (childSize.x > layoutWidth.i) {
                        layoutWidth = childSize.x
                    }
                }

                if (node.horizontalAlignment !== HorizontalAlignment.LEFT) {
                    layout.forEach { child ->
                        child.pos.x = node.horizontalAlignment.align(child.size.x, node.size.x - padding.horizontal)
                    }
                }

                if (node.verticalAlignment !== VerticalAlignment.TOP) {
                    layout.forEach { child ->
                        if (node.verticalAlignment == VerticalAlignment.CENTER) {
                            child.pos.y = (node.size.y - padding.vertical - layoutHeight.i) / 2
                        } else {
                            child.pos.y = (node.size.y - padding.vertical - layoutHeight.i)
                        }
                    }
                }
            }

            val LTR_TEXT = Algorithm { node: FlowLayout ->
                var layoutWidth = 0
                var layoutHeight = 0
                var rowWidth = 0
                var rowOffset = 0
                val layout = ArrayList<Node>()
                val padding = node.padding
                val childSpace = node.calculateChildSpace()

                node.children().forEach { child: Node ->
                    child.inflate(childSpace)
                }

                node.children().forEach { child ->
                    layout.add(child)
                    var x = node.pos.x + padding.left + rowWidth
                    var y = node.pos.y + padding.top + rowOffset
                    val childSize = child.size
                    if (x + childSize.x > childSpace.x) {
                        x -= rowWidth
                        y = y - rowOffset + layoutHeight
                        rowOffset = layoutHeight
                        rowWidth = 0
                    }
                    child.pos = Vec2i(x, y)
                    rowWidth += childSize.x
                    if (rowOffset + childSize.y > layoutHeight) {
                        layoutHeight = rowOffset + childSize.y
                    }
                    if (rowWidth.i > layoutWidth.i) {
                        layoutWidth = rowWidth
                    }
                }

                if (node.horizontalAlignment != HorizontalAlignment.LEFT) {
                    layout.forEach { child ->
                        child.pos.x = node.horizontalAlignment.align(child.size.x, node.size.x - padding.horizontal)
                    }
                }

                if (node.verticalAlignment != VerticalAlignment.TOP) {
                    layout.forEach { child ->
                        if (node.verticalAlignment == VerticalAlignment.CENTER) {
                            child.pos.y = (node.size.y - padding.vertical - layoutHeight.i) / 2
                        } else {
                            child.pos.y = node.size.y - padding.vertical - layoutHeight.i
                        }
                    }
                }
            }
        }

        operator fun invoke(node: FlowLayout)
    }
}