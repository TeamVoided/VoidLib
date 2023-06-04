package org.teamvoided.voidlib.vui.v2.rendering

import com.mojang.blaze3d.systems.RenderSystem
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i

class SimpleScissorBox(override val pos: Vec2i, override val size: Vec2i): ScissorBox() {
    override fun subBox(pos: Vec2i, size: Vec2i): ScissorBox {
        val (realPos, realSize) = subBoxParams(pos, size)

        return SimpleScissorBox(realPos, realSize)
    }

    override fun start(): ScissorBox {
        RenderSystem.enableScissor(pos.x, pos.y, size.x, size.y)
        return this
    }

    override fun end(): ScissorBox {
        RenderSystem.disableScissor()
        return this
    }
}