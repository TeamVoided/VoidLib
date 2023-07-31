package org.teamvoided.voidlib.vui.v2.node.layout

import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i

interface BoundingBox {
    var pos: Vec2i
    var size: Vec2i

    companion object {
        fun of(pos: Vec2i, size: Vec2i): BoundingBox {
            return object : BoundingBox {
                override var pos = pos
                override var size = size
            }
        }
    }

    fun isTouching(point: Vec2i): Boolean {
        return point.x >= this.pos.x && point.x <= this.pos.x + size.x &&
                point.y >= this.pos.y && point.y <= this.pos.y + size.y
    }

    fun isTouching(point: Vec2f): Boolean {
        return point.x >= this.pos.x && point.x <= this.pos.x + size.x &&
                point.y >= this.pos.y && point.y <= this.pos.y + size.y
    }

    fun isTouching(point: Vec2d): Boolean {
        return point.x >= this.pos.x && point.x <= this.pos.x + size.x &&
                point.y >= this.pos.y && point.y <= this.pos.y + size.y
    }

    fun inflate(pos: Vec2i, size: Vec2i) {
        this.pos = pos
        this.size = size
    }
}