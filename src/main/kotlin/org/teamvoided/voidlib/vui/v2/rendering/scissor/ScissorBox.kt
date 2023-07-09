package org.teamvoided.voidlib.vui.v2.rendering.scissor

import net.minecraft.client.MinecraftClient
import org.lwjgl.opengl.GL11
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.vui.v2.node.layout.BoundingBox
import kotlin.math.max
import kotlin.math.min

abstract class ScissorBox: BoundingBox {

    open fun subBox(pos: Vec2i, size: Vec2i): ScissorBox {
        val (subPos, subSize) = subBoxParams(pos, size)

        return ClosingScissorBox(subPos, subSize)
    }

    protected fun subBoxParams(pos: Vec2i, size: Vec2i): Pair<Vec2i, Vec2i> {
        var sY = this.pos.y.coerceAtLeast(pos.y)
        val eY = (this.pos.y + this.size.y).coerceAtLeast(pos.y + size.y)
        val sX = this.pos.x.coerceAtLeast(pos.x)
        var eX = (this.pos.x + this.size.x).coerceAtLeast(pos.x + size.x)

        sY = min(sY, eY)
        eX = min(sX, eX)

        return Pair(Vec2i(sX, sY), Vec2i(eX - sX, eY - sY))
    }

    open fun intersects(other: ScissorBox): Boolean {
        return other.pos.x < pos.x + size.x && other.pos.x + other.size.x >= pos.x && other.pos.y < pos.y + size.y && other.pos.y + other.size.y >= pos.y
    }

    open fun intersection(other: ScissorBox): ScissorBox {
        val leftEdge = max(pos.x.d, other.pos.x.d).i
        val topEdge = max(pos.y.d, other.pos.y.d).i
        val rightEdge = min((pos.x + size.x).d, (other.pos.x + other.size.x).d).i
        val bottomEdge = min((pos.y + size.y).d, (other.pos.y + other.size.y).d).i
        return SimpleScissorBox(
            Vec2i(leftEdge, topEdge),
            Vec2i(max((rightEdge - leftEdge).d, 0.0).i, max((bottomEdge - topEdge).d, 0.0).i)
        )
    }

    open fun start(): ScissorBox {
        GL11.glScissor(pos.x, pos.y, size.x, size.y)
        return this
    }

    fun start(scale: Double): ScissorBox {
        GL11.glScissor(
            (pos.x * scale).i, (pos.y * scale - size.y * scale).i,
            (size.x * scale).i, (size.y * scale).i
        )

        return this
    }

    fun start(scale: Double, framebufferHeight: Int): ScissorBox {
        GL11.glScissor(
            (pos.x * scale).i, (framebufferHeight - pos.y * scale - size.y * scale).i,
            (size.x * scale).i, (size.y * scale).i
        )

        return this
    }

    open fun applyScale(scale: Double, framebufferHeight: Int, scaleMethod: ScaleMethod) {
        pos.x = if (scaleMethod == ScaleMethod.MULTIPLICATION) (pos.x * scale).i else (pos.x * (1 / scale)).i
        pos.y = if (scaleMethod == ScaleMethod.MULTIPLICATION) (framebufferHeight - pos.y * scale - size.y * scale).i else (framebufferHeight - pos.y * (1 / scale) - size.y * (1 / scale)).i
        size.x = if (scaleMethod == ScaleMethod.MULTIPLICATION) (size.x * scale).i else (size.x * (1 / scale)).i
        size.y = if (scaleMethod == ScaleMethod.MULTIPLICATION) (size.y * scale).i else (size.y * (1 / scale)).i
    }

    open fun withScale(scale: Double, framebufferHeight: Int, scaleMethod: ScaleMethod): ScissorBox {
        val oPos = Vec2i(
            if (scaleMethod == ScaleMethod.MULTIPLICATION) (pos.x * scale).i else (pos.x * (1 / scale)).i,
            if (scaleMethod == ScaleMethod.MULTIPLICATION) (framebufferHeight - pos.y * scale - size.y * scale).i else (framebufferHeight - pos.y * (1 / scale) - size.y * (1 / scale)).i
        )
        val oSize = Vec2i(
            if (scaleMethod == ScaleMethod.MULTIPLICATION) (size.x * scale).i else (size.x * (1 / scale)).i,
            if (scaleMethod == ScaleMethod.MULTIPLICATION) (size.y * scale).i else (size.y * (1 / scale)).i
        )

        return SimpleScissorBox(oPos, oSize)
    }

    open fun end(): ScissorBox {
        val window = MinecraftClient.getInstance().window
        GL11.glScissor(0, 0, window.framebufferWidth, window.framebufferHeight)
        return this
    }

    enum class ScaleMethod { MULTIPLICATION, DIVISION }
}