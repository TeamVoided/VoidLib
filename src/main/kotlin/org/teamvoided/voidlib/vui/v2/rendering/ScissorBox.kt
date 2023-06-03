package org.teamvoided.voidlib.vui.v2.rendering

import org.teamvoided.voidlib.core.datastructures.Vec2i
import kotlin.math.min

abstract class ScissorBox {
    abstract val pos: Vec2i
    abstract val size: Vec2i

    abstract fun subBox(pos: Vec2i, size: Vec2i): ScissorBox
    protected fun subBoxParams(pos: Vec2i, size: Vec2i): Pair<Vec2i, Vec2i> {
        var sY = this.pos.y.coerceAtLeast(pos.y)
        val eY = (this.pos.y + this.size.y).coerceAtLeast(pos.y + size.y)
        val sX = this.pos.x.coerceAtLeast(pos.x)
        var eX = (this.pos.x + this.size.x).coerceAtLeast(pos.x + size.x)

        sY = min(sY, eY)
        eX = min(sX, eX)

        return Pair(Vec2i(sX, sY), Vec2i(eX - sX, eY - sY))
    }
    abstract fun start(): ScissorBox
    abstract fun end(): ScissorBox
}