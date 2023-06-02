package org.teamvoided.voidlib.vui.v2

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.Vec2i

object DrawHelper: DrawableHelper() {
    fun drawRect(matrices: MatrixStack, pos: Vec2i, size: Vec2i, color: ARGB) = drawGradientRect(matrices, pos, size, color, color, color, color)

    fun drawGradientRect(matrices: MatrixStack, pos: Vec2i, size: Vec2i, topLeftColor: ARGB, topRightColor: ARGB, bottomLeftColor: ARGB, bottomRightColor: ARGB) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, pos.x + size.x.toFloat(), pos.y.toFloat(), 0f).color(topRightColor.toInt()).next()
        buffer.vertex(matrix, pos.x.toFloat(), pos.y.toFloat(), 0f).color(topLeftColor.toInt()).next()
        buffer.vertex(matrix, pos.x.toFloat(), pos.y + size.y.toFloat(), 0f).color(bottomLeftColor.toInt()).next()
        buffer.vertex(matrix, pos.x + size.x.toFloat(), pos.y + size.y.toFloat(), 0f).color(bottomRightColor.toInt()).next()

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        Tessellator.getInstance().draw()

        RenderSystem.disableBlend()
    }
}