package org.teamvoided.voidlib.vui.v2.rendering

import com.google.common.base.Preconditions
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.joml.Matrix4f
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.impl.Vui
import org.teamvoided.voidlib.vui.mixin.ScreenInvoker
import org.teamvoided.voidlib.vui.v2.event.fabric.WindowResizeCallback
import org.teamvoided.voidlib.vui.v2.geomentry.Geometry
import kotlin.math.cos
import kotlin.math.sin


object Pencil: DrawableHelper() {
    fun drawRectOutline(matrices: MatrixStack, pos: Vec2i, size: Vec2i, color: ARGB) {
        val cInt = color.toInt()
        fill(matrices, pos.x, pos.y, pos.x + size.x, pos.y + 1, cInt)
        fill(matrices, pos.x, pos.y + size.y - 1, pos.x + size.x, pos.y + size.y, cInt)

        fill(matrices, pos.x, pos.y + 1, pos.x + 1, pos.y + size.y - 1, cInt)
        fill(matrices, pos.x + size.x - 1, pos.y + 1, pos.x + size.x, pos.y + size.y - 1, cInt)
    }

    fun drawRect(matrices: MatrixStack, pos: Vec2f, size: Vec2f, color: ARGB) = drawGradientRect(matrices, pos, size, color, color, color, color)

    fun drawGradientRect(matrices: MatrixStack, pos: Vec2f, size: Vec2f, topLeftColor: ARGB, topRightColor: ARGB, bottomLeftColor: ARGB, bottomRightColor: ARGB) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, pos.x + size.x, pos.y, 0f).color(topRightColor.toInt()).next()
        buffer.vertex(matrix, pos.x, pos.y, 0f).color(topLeftColor.toInt()).next()
        buffer.vertex(matrix, pos.x, pos.y + size.y, 0f).color(bottomLeftColor.toInt()).next()
        buffer.vertex(matrix, pos.x + size.x, pos.y + size.y, 0f).color(bottomRightColor.toInt()).next()

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        Tessellator.getInstance().draw()

        RenderSystem.disableBlend()
    }

    fun drawSpectrum(matrices: MatrixStack, pos: Vec2f, size: Vec2f, vertical: Boolean) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, pos.x, pos.y, 0f).color(1f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, pos.x, (pos.y + size.x), 0f).color(if (vertical) 0f else 1f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, (pos.x + size.x), (pos.y + size.y), 0f).color(0f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, (pos.x + size.y), pos.y, 0f).color(if (vertical) 1f else 0f, 1f, 1f, 1f).next()

        Vui.hsvProgram.use()
        Tessellator.getInstance().draw()
    }

    fun drawText(matrices: MatrixStack, text: Text, pos: Vec2i, scale: Float, color: ARGB) {
        drawText(matrices, text, pos, scale, color, TextAnchor.TOP_LEFT)
    }

    fun drawText(matrices: MatrixStack, text: Text, pos: Vec2i, scale: Float, color: ARGB, anchorPoint: TextAnchor) {
        var x = pos.x.toFloat()
        var y = pos.y.toFloat()

        val textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
        matrices.push()
        matrices.scale(scale, scale, 1f)
        when (anchorPoint) {
            TextAnchor.TOP_RIGHT -> x -= textRenderer.getWidth(text) * scale
            TextAnchor.BOTTOM_LEFT -> y -= textRenderer.fontHeight * scale
            TextAnchor.BOTTOM_RIGHT -> { x -= textRenderer.getWidth(text) * scale; y -= textRenderer.fontHeight * scale }
            TextAnchor.TOP_LEFT -> {}
        }

        textRenderer.draw(matrices, text, x * (1 / scale), y * (1 / scale), color.toInt())
        matrices.pop()
    }

    fun drawLine(matrices: MatrixStack, from: Vec2f, to: Vec2f, thiccness: Float, color: ARGB) {
        val offset = Vec2f(to.x - from.x, to.x - from.x).perpendicular().normalize() * (thiccness * .5f)
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val cInt = color.toInt()

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, from.x + offset.x, from.y + offset.y, 0f).color(cInt).next()
        buffer.vertex(matrix, from.x - offset.x, from.y - offset.y, 0f).color(cInt).next()
        buffer.vertex(matrix, to.x - offset.x, to.y - offset.y, 0f).color(cInt).next()
        buffer.vertex(matrix, to.x + offset.x, to.y + offset.y, 0f).color(cInt).next()

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        Tessellator.getInstance().draw()
    }

    fun drawCircle(matrices: MatrixStack, center: Vec2f, segments: Int, radius: Double, color: ARGB) =
        drawCircle(matrices, center, 0.0, 360.0, segments, radius, color)

    fun drawCircle(matrices: MatrixStack, center: Vec2f, angleFrom: Double, angleTo: Double, segments: Int, radius: Double, color: ARGB) {
        Preconditions.checkArgument(angleFrom < angleTo, "angleFrom must be less than angleTo")

        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val angleStep = Math.toRadians(angleTo - angleFrom) / segments
        val cInt = color.toInt()

        buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, center.x, center.y, 0f).color(cInt).next()

        for (i in segments downTo 0) {
            val theta = Math.toRadians(angleFrom) + i * angleStep
            buffer.vertex(matrix, (center.x - cos(theta) * radius).toFloat(), (center.y - sin(theta) * radius).toFloat(), 0f).color(cInt).next()
        }

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        Tessellator.getInstance().draw()
    }

    fun drawRing(
        matrices: MatrixStack,
        center: Vec2f,
        angleFrom: Double, angleTo: Double,
        segments: Int,
        innerRadius: Double, outerRadius: Double,
        innerColor: ARGB, outerColor: ARGB
    ) {
        Preconditions.checkArgument(angleFrom < angleTo, "angleFrom must be less than angleTo");
        Preconditions.checkArgument(innerRadius < outerRadius, "innerRadius must be less than outerRadius");

        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val angleStep = Math.toRadians(angleTo - angleFrom) / segments
        val cIntInner = innerColor.toInt()
        val cIntOuter = outerColor.toInt()

        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR)

        for (i in 0..segments) {
            val theta = Math.toRadians(angleFrom) + i * angleStep
            buffer.vertex(matrix, (center.x - cos(theta) * outerRadius).toFloat(), (center.y - sin(theta) * outerRadius).toFloat(), 0f).color(cIntOuter).next()
            buffer.vertex(matrix, (center.x - cos(theta) * innerRadius).toFloat(), (center.y - sin(theta) * innerRadius).toFloat(), 0f).color(cIntInner).next()
        }

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        Tessellator.getInstance().draw()
    }

    fun drawGeometry(matrices: MatrixStack, pos: Vec2f, scale: Float, geometry: Geometry) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val defCInt = ARGB(255, 77, 77, 77).toInt()

        buffer.begin(geometry.type.drawMode, VertexFormats.POSITION_COLOR)

        for (face in geometry.faces) {
            face.vertices.forEachIndexed { index, vertex ->
                buffer.vertex(matrix, (pos.x + vertex.x) * scale, (pos.y + vertex.y) * scale, vertex.z * scale).color(if (face.hasColor) face.colors[index].toARGB().toInt() else defCInt).next()
            }
        }

        RenderSystem.enableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        Tessellator.getInstance().draw()

        RenderSystem.disableDepthTest()
    }

    fun drawToolTip(matrices: MatrixStack, pos: Vec2i, tooltip: List<TooltipComponent>) {
        (UtilityScreen as ScreenInvoker).void_renderTooltipFromComponents(matrices, tooltip, pos.x, pos.y, HoveredTooltipPositioner.INSTANCE)
    }

    fun fillGradient(matrices: MatrixStack, start: Vec2i, end: Vec2i, colorStart: ARGB, colorEnd: ARGB) {
        fillGradient(matrices, start.x, start.y, end.x, end.y, colorStart.toInt(), colorEnd.toInt(), 0)
    }

    fun fillGradient(matrix: Matrix4f, builder: BufferBuilder, start: Vec2i, end: Vec2i, z: Int, colorStart: ARGB, colorEnd: ARGB) {
        fillGradient(matrix, builder, start.x, start.y, end.x, end.y, z, colorStart.toInt(), colorEnd.toInt())
    }

    fun fillGradient(matrices: MatrixStack, start: Vec2i, end: Vec2i, colorStart: ARGB, colorEnd: ARGB, z: Int) {
        fillGradient(matrices, start.x, start.y, end.x, end.y, colorStart.toInt(), colorEnd.toInt(), z)
    }

    enum class TextAnchor {
        TOP_RIGHT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        BOTTOM_LEFT
    }

    object UtilityScreen: Screen(Text.empty()) {
        init {
            val client = MinecraftClient.getInstance()
            init(client, client.window.scaledWidth, client.window.scaledHeight)
            WindowResizeCallback.event.register { cl, window ->
                init(cl, window.scaledWidth, window.scaledHeight)
            }
        }
    }
}