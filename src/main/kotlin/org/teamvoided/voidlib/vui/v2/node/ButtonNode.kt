package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.datastructures.vector.Vec3i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle

open class ButtonNode(var message: Text, var renderer: Renderer = Renderer.VANILLA, var onPress: (ButtonNode) -> Unit): Node() {
    var active = true
    var textShadow = true
    var hovered = false

    companion object {
        val ACTIVE_TEXTURE = id("vres/button/active")
        val HOVERED_TEXTURE = id("vres/button/hovered")
        val DISABLED_TEXTURE = id("vres/button/disabled")
    }

    constructor(pos: Vec2i, message: Text, onPress: (ButtonNode) -> Unit): this(message, Renderer.VANILLA, onPress) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, message: Text, onPress: (ButtonNode) -> Unit): this(message, Renderer.VANILLA, onPress) {
        this.pos = pos
        this.size = size
    }

    constructor(pos: Vec2i, message: Text, renderer: Renderer, onPress: (ButtonNode) -> Unit): this(message, renderer, onPress) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, message: Text, renderer: Renderer, onPress: (ButtonNode) -> Unit): this(message, renderer, onPress) {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent): Boolean {
        if (hovered) onPress(this)

        return false
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        hovered = isTouching(event.updateContext.mousePos)
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        val matrices = event.drawContext.matrices
        renderer.draw(matrices, this, event.drawContext.delta)

        val textRenderer = MinecraftClient.getInstance().textRenderer
        val color = if (active) ARGB(255, 255, 255) else ARGB(160, 160, 160)

        if (textShadow) {
            Pencil.drawCenteredTextWithShadow(matrices, textRenderer, message, globalPos.x + size.x / 2, globalPos.y + (size.y - 8) / 2, color)
        } else {
            textRenderer.draw(matrices, message, globalPos.x + size.x / 2f - textRenderer.getWidth(message) / 2f, globalPos.y + (size.y - 8) / 2f, color.toInt())
        }
    }

    override fun cursorStyle(): CursorStyle {
        return if (hovered) CursorStyle.HAND else CursorStyle.NONE
    }

    fun interface Renderer {
        companion object {
            val VANILLA = Renderer { matrices, button, _ ->
                RenderSystem.enableDepthTest()
                val texture = if (button.active) if (button.hovered) HOVERED_TEXTURE else ACTIVE_TEXTURE else DISABLED_TEXTURE
                val sprite = VuiSpriteManager.getSprite(texture) ?: throw IllegalStateException("Sprite could not be found")
                RenderSystem.setShaderTexture(0, sprite.atlasId)
                Pencil.drawSprite(matrices, Vec3i(button.globalPos.x, button.globalPos.y, 0), button.size, sprite)
            }
        }

        fun flat(
            color: ARGB,
            hoveredColor: ARGB,
            disabledColor: ARGB
        ): Renderer {
            return Renderer { matrices: MatrixStack, button: ButtonNode, _: Float ->
                RenderSystem.enableDepthTest()
                if (button.active) {
                    if (button.hovered) {
                        Pencil.drawRect(matrices, button.globalPos.to2f(), button.size.to2f(), hoveredColor)
                    } else {
                        Pencil.drawRect(matrices, button.globalPos.to2f(), button.size.to2f(), color)
                    }
                } else {
                    Pencil.drawRect(matrices, button.globalPos.to2f(), button.size.to2f(), disabledColor)
                }
            }
        }

        fun texture(
            texture: Identifier,
            uv: Vec2f,
            textureSize: Vec2i
        ): Renderer {
            return Renderer { matrices: MatrixStack, button: ButtonNode, _: Float ->
                var renderV = uv.y
                if (!button.active) {
                    renderV += button.size.x * 2
                } else if (button.hovered) {
                    renderV += button.size.y
                }
                RenderSystem.enableDepthTest()
                RenderSystem.setShaderTexture(0, texture)
                Pencil.drawTexture(
                    matrices,
                    button.globalPos,
                    Vec2f(uv.x, renderV),
                    button.size,
                    textureSize
                )
            }
        }

        fun draw(matrices: MatrixStack, button: ButtonNode, delta: Float)
    }
}