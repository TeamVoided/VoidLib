package org.teamvoided.voidlib.vui.v2.rendering

import com.google.common.base.Preconditions
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.gui.tooltip.TooltipPositioner
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.joml.Matrix4f
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.datastructures.vector.Vec3i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.vui.mixin.VanillaDrawContextInvoker
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.geomentry.Geometry
import org.teamvoided.voidlib.vui.v2.rendering.scissor.ScissorBox
import org.teamvoided.voidlib.vui.v2.shader.Shaders
import kotlin.math.cos
import kotlin.math.sin

object Pencil {
    private var currentScissorBox: ScissorBox? = null

    fun drawRectOutline(drawContext: DrawContext, pos: Vec2i, size: Vec2i, color: ARGB) {
        val cInt = color.toInt()
        drawContext.vanillaInstance.fill(pos.x, pos.y, pos.x + size.x, pos.y + 1, cInt)
        drawContext.vanillaInstance.fill(pos.x, pos.y + size.y - 1, pos.x + size.x, pos.y + size.y, cInt)
        drawContext.vanillaInstance.fill(pos.x, pos.y + 1, pos.x + 1, pos.y + size.y - 1, cInt)
        drawContext.vanillaInstance.fill(pos.x + size.x - 1, pos.y + 1, pos.x + size.x, pos.y + size.y - 1, cInt)
    }

    fun drawRect(drawContext: DrawContext, pos: Vec2f, size: Vec2f, color: ARGB) = drawGradientRect(drawContext, pos, size, color, color, color, color)

    fun drawGradientRect(drawContext: DrawContext, pos: Vec2f, size: Vec2f, topLeftColor: ARGB, topRightColor: ARGB, bottomLeftColor: ARGB, bottomRightColor: ARGB) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = drawContext.matrices.peek().positionMatrix

        buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
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

    fun drawSpectrum(drawContext: DrawContext, pos: Vec2f, size: Vec2f, vertical: Boolean) {
        val buffer = Tessellator.getInstance().buffer
        val matrix = drawContext.matrices.peek().positionMatrix

        buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, pos.x, pos.y, 0f).color(1f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, pos.x, (pos.y + size.y), 0f).color(if (vertical) 0f else 1f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, (pos.x + size.x), (pos.y + size.y), 0f).color(0f, 1f, 1f, 1f).next()
        buffer.vertex(matrix, (pos.x + size.x), pos.y, 0f).color(if (vertical) 1f else 0f, 1f, 1f, 1f).next()

        Shaders.hsvProgram.use()
        Tessellator.getInstance().draw()
    }

    fun drawText(drawContext: DrawContext, text: Text, pos: Vec2i, scale: Float, color: ARGB) {
        drawText(drawContext, text, pos, scale, color, TextAnchor.TOP_LEFT)
    }

    fun drawText(drawContext: DrawContext, text: Text, pos: Vec2i, scale: Float, color: ARGB, anchorPoint: TextAnchor) {
        var x = pos.x.f
        var y = pos.y.f

        val textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
        drawContext.matrices.push()
        drawContext.matrices.scale(scale, scale, 1f)
        when (anchorPoint) {
            TextAnchor.TOP_RIGHT -> x -= textRenderer.getWidth(text) * scale
            TextAnchor.BOTTOM_LEFT -> y -= textRenderer.fontHeight * scale
            TextAnchor.BOTTOM_RIGHT -> { x -= textRenderer.getWidth(text) * scale; y -= textRenderer.fontHeight * scale }
            TextAnchor.TOP_LEFT -> {}
        }

        textRenderer.draw(text.asOrderedText(), x * (1 / scale), y * (1 / scale), (textRenderer.getWidth(text) * scale).i, true, drawContext.matrices.peek().positionMatrix, drawContext.vanillaInstance.vertexConsumers, TextLayerType.NORMAL, 0, color.toInt())
        drawContext.matrices.pop()
    }

    fun drawLine(matrices: MatrixStack, from: Vec2f, to: Vec2f, thiccness: Float, color: ARGB) {
        val offset = Vec2f(to.x - from.x, to.x - from.x).perpendicular().normalize() * (thiccness * .5f)
        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val cInt = color.toInt()

        buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
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

        buffer.begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, center.x, center.y, 0f).color(cInt).next()

        for (i in segments downTo 0) {
            val theta = Math.toRadians(angleFrom) + i * angleStep
            buffer.vertex(matrix, (center.x - cos(theta) * radius).f, (center.y - sin(theta) * radius).f, 0f).color(cInt).next()
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
        Preconditions.checkArgument(angleFrom < angleTo, "angleFrom must be less than angleTo")
        Preconditions.checkArgument(innerRadius < outerRadius, "innerRadius must be less than outerRadius")

        val buffer = Tessellator.getInstance().buffer
        val matrix = matrices.peek().positionMatrix
        val angleStep = Math.toRadians(angleTo - angleFrom) / segments
        val cIntInner = innerColor.toInt()
        val cIntOuter = outerColor.toInt()

        buffer.begin(DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR)

        for (i in 0..segments) {
            val theta = Math.toRadians(angleFrom) + i * angleStep
            buffer.vertex(matrix, (center.x - cos(theta) * outerRadius).f, (center.y - sin(theta) * outerRadius).f, 0f).color(cIntOuter).next()
            buffer.vertex(matrix, (center.x - cos(theta) * innerRadius).f, (center.y - sin(theta) * innerRadius).f, 0f).color(cIntInner).next()
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
        DiffuseLighting.enableGuiDepthLighting()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        Tessellator.getInstance().draw()
    }

    fun drawToolTip(drawContext: DrawContext, textRenderer: TextRenderer, pos: Vec2i, tooltip: List<TooltipComponent>, tooltipPositioner: TooltipPositioner = HoveredTooltipPositioner.INSTANCE) {
        (drawContext.vanillaInstance as VanillaDrawContextInvoker).void_drawTooltip(textRenderer, tooltip, pos.x, pos.y, tooltipPositioner)
    }

    fun enableScissorBox(scissorBox: ScissorBox) {
        currentScissorBox = if (currentScissorBox == null) {
            scissorBox.start()
            scissorBox
        } else {
            disableScissorBox()
            scissorBox.start()
            scissorBox
        }
    }

    fun disableScissorBox() {
        currentScissorBox?.end()
        currentScissorBox = null
    }

    fun drawItem(drawContext: DrawContext, itemStack: ItemStack, pos: Vec2i) =
        drawContext.vanillaInstance.drawItem(itemStack, pos.x, pos.y)

    fun drawItem(drawContext: DrawContext, itemStack: ItemStack, pos: Vec2i, k: Int) =
        drawContext.vanillaInstance.drawItem(itemStack, pos.x, pos.y, k)

    fun drawItem(drawContext: DrawContext, itemStack: ItemStack, pos: Vec2i, size: Vec2i) =
        drawContext.vanillaInstance.drawItem(itemStack, pos.x, pos.y, pos.x + size.x, pos.y + size.y)

    fun drawGuiItem(drawContext: DrawContext, itemStack: ItemStack, pos: Vec2i) =
        drawContext.vanillaInstance.drawItemWithoutEntity(itemStack, pos.x, pos.y)

    fun drawItem(drawContext: DrawContext, livingEntity: LivingEntity, itemStack: ItemStack, pos: Vec2i, k: Int) =
        drawContext.vanillaInstance.drawItem(livingEntity, itemStack, pos.x, pos.y, k)

    fun drawItem(drawContext: DrawContext, livingEntity: LivingEntity, world: World, itemStack: ItemStack, pos: Vec2i, k: Int) =
        (drawContext.vanillaInstance as VanillaDrawContextInvoker).void_drawItem(livingEntity, world, itemStack, pos.x, pos.y, k, 0)

    fun drawItem(drawContext: DrawContext, livingEntity: LivingEntity, world: World, itemStack: ItemStack, pos: Vec2i, k: Int, l: Int) =
        (drawContext.vanillaInstance as VanillaDrawContextInvoker).void_drawItem(livingEntity, world, itemStack, pos.x, pos.y, k, l)

    fun drawItemInSlot(drawContext: DrawContext, textRenderer: TextRenderer, itemStack: ItemStack, pos: Vec2i) =
        drawContext.vanillaInstance.drawItemInSlot(textRenderer, itemStack, pos.x, pos.y)

    fun drawItemInSlot(drawContext: DrawContext, textRenderer: TextRenderer, itemStack: ItemStack, pos: Vec2i, string: String) =
        drawContext.vanillaInstance.drawItemInSlot(textRenderer, itemStack, pos.x, pos.y, string)

    fun drawCenteredTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: String, centerX: Int, y: Int, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, centerX, y, color.toInt())

    fun drawCenteredTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: Text, centerX: Int, y: Int, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, centerX, y, color.toInt())

    fun drawCenteredTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: OrderedText, centerX: Int, y: Int, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, centerX, y, color.toInt())

    fun drawTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: String, pos: Vec2i, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, pos.x, pos.y, color.toInt())

    fun drawTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: Text, pos: Vec2i, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, pos.x, pos.y, color.toInt())

    fun drawTextWithShadow(drawContext: DrawContext, textRenderer: TextRenderer, text: OrderedText, pos: Vec2i, color: ARGB) =
        drawContext.vanillaInstance.drawCenteredTextWithShadow(textRenderer, text, pos.x, pos.y, color.toInt())

    fun drawSprite(drawContext: DrawContext, pos: Vec3i, size: Vec2i, sprite: Sprite) =
        drawContext.vanillaInstance.drawSprite(pos.x, pos.y, pos.z, size.x, size.y, sprite)

    fun drawSprite(drawContext: DrawContext, pos: Vec3i, size: Vec2i, sprite: Sprite, color: ARGB) =
        drawContext.vanillaInstance.drawSprite(
            pos.x, pos.y, pos.z,
            size.x, size.y,
            sprite,
            color.red.f / 255f, color.green.f / 255f, color.blue.f / 255f, color.alpha.f / 255f
        )

    fun drawBorder(drawContext: DrawContext, pos: Vec2i, size: Vec2i, color: ARGB) =
        drawContext.vanillaInstance.drawBorder(pos.x, pos.y, size.x, size.y, color.toInt())

    fun drawTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, uv: Vec2i, size: Vec2i) =
        drawContext.vanillaInstance.drawTexture(texture, pos.x, pos.y, uv.x, uv.y, size.x, size.y)

    fun drawTexture(drawContext: DrawContext, texture: Identifier, pos: Vec3i, uv: Vec2f, size: Vec2i, textureSize: Vec2i) =
        drawContext.vanillaInstance.drawTexture(texture, pos.x, pos.y, pos.z, uv.x, uv.y, size.x, size.y, textureSize.x, textureSize.y)

    fun drawTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, size: Vec2i, uv: Vec2f, regionSize: Vec2i, textureSize: Vec2i) =
        drawContext.vanillaInstance.drawTexture(texture, pos.x, pos.y, size.x, size.y, uv.x, uv.y, regionSize.x, regionSize.y, textureSize.x, textureSize.y)

    fun drawTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, uv: Vec2f, size: Vec2i, textureSize: Vec2i) =
        drawContext.vanillaInstance.drawTexture(texture, pos.x, pos.y, uv.x, uv.y, size.x, size.y, textureSize.x, textureSize.y)

    fun drawTexture(drawContext: DrawContext, texture: Identifier, pos1: Vec2i, pos2: Vec2i, z: Int, regionSize: Vec2i, uv: Vec2f, textureSize: Vec2i) =
        (drawContext.vanillaInstance as VanillaDrawContextInvoker).void_drawTexture(texture, pos1.x, pos2.x, pos1.y, pos2.y, z, regionSize.x, regionSize.y, uv.x, uv.y, textureSize.x, textureSize.y)

    fun drawTexturedQuad(drawContext: DrawContext, texture: Identifier, pos1: Vec2i, pos2: Vec2i, z: Int, uv1: Vec2f, uv2: Vec2f) {
        RenderSystem.setShaderTexture(0, texture)
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        val matrix4f: Matrix4f = drawContext.matrices.peek().positionMatrix
        val bufferBuilder = Tessellator.getInstance().buffer
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
        bufferBuilder.vertex(matrix4f, pos1.x.toFloat(), pos1.y.toFloat(), z.toFloat()).texture(uv1.x, uv1.y).next()
        bufferBuilder.vertex(matrix4f, pos1.x.toFloat(), pos2.y.toFloat(), z.toFloat()).texture(uv1.x, uv2.y).next()
        bufferBuilder.vertex(matrix4f, pos2.x.toFloat(), pos2.y.toFloat(), z.toFloat()).texture(uv2.x, uv2.y).next()
        bufferBuilder.vertex(matrix4f, pos2.x.toFloat(), pos1.y.toFloat(), z.toFloat()).texture(uv2.x, uv1.y).next()
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end())
    }

    fun drawTexturedQuad(drawContext: DrawContext, texture: Identifier, pos1: Vec2i, pos2: Vec2i, z: Int, uv1: Vec2f, uv2: Vec2f, color: ARGB) =
        (drawContext.vanillaInstance as VanillaDrawContextInvoker).void_drawTexturedQuad(
            texture,
            pos1.x, pos2.x,
            pos1.y, pos2.y, z,
            uv1.x, uv2.x,
            uv1.y, uv2.y,
            color.red.f / 255f, color.green.f / 255f, color.blue.f / 255f, color.alpha.f / 255f
        )

    fun drawNineSlicedTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, size: Vec2i, outerSliceSize: Int, centerSliceSize: Vec2i, uv: Vec2i) =
        drawContext.vanillaInstance.drawNineSlicedTexture(texture, pos.x, pos.y, size.x, size.y, outerSliceSize, centerSliceSize.x, centerSliceSize.y, uv.x, uv.y)

    fun drawNineSlicedTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, size: Vec2i, sideSliceSize: Vec2i, centerSliceSize: Vec2i, uv: Vec2i) =
        drawContext.vanillaInstance.drawNineSlicedTexture(texture, pos.x, pos.y, size.x, size.y, sideSliceSize.x, sideSliceSize.y, centerSliceSize.x, centerSliceSize.y, uv.x, uv.y)

    fun drawNineSlicedTexture(
        drawContext: DrawContext,
        texture: Identifier,
        pos: Vec2i,
        size: Vec2i,
        leftSliceWidth: Int,
        topSliceHeight: Int,
        rightSliceWidth: Int,
        bottomSliceHeight: Int,
        centerSliceSize: Vec2i,
        uv: Vec2i
    ) = drawContext.vanillaInstance.drawNineSlicedTexture(
        texture,
        pos.x, pos.y,
        size.x, size.y,
        leftSliceWidth,
        topSliceHeight,
        rightSliceWidth,
        bottomSliceHeight,
        centerSliceSize.x, centerSliceSize.y,
        uv.x, uv.y
    )

    fun drawRepeatingTexture(drawContext: DrawContext, texture: Identifier, pos: Vec2i, size: Vec2i, uv: Vec2i, textureSize: Vec2i) =
        drawContext.vanillaInstance.drawRepeatingTexture(texture, pos.x, pos.y, size.x, size.y, uv.x, uv.y, textureSize.x, textureSize.y)


    enum class TextAnchor {
        TOP_RIGHT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        BOTTOM_LEFT
    }
}