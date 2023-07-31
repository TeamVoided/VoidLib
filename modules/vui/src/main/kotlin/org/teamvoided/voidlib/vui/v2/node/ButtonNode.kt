package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.datastructures.vector.Vec3i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEvent.DrawEvent
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEvent.UpdateEvent
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.node.ButtonNode.Renderer
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle

open class ButtonNode(var message: Text, var renderer: Renderer = Renderer.VANILLA): Node() {
    var active = true
    var textShadow = true
    var hovered = false

    val buttonPressCallback: Callback<ButtonNode>
        get() = getCallbackAs(id("button_press_callback"))

    companion object {
        val ACTIVE_TEXTURE = id("vui", "vres/button/active")
        val HOVERED_TEXTURE = id("vui", "vres/button/hovered")
        val DISABLED_TEXTURE = id("vui", "vres/button/disabled")
    }

    constructor(pos: Vec2i, message: Text): this(message, Renderer.VANILLA) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, message: Text): this(message, Renderer.VANILLA) {
        this.pos = pos
        this.size = size
    }

    constructor(pos: Vec2i, message: Text, renderer: Renderer): this(message, renderer) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, message: Text, renderer: Renderer): this(message, renderer) {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (hovered) {
            buttonPressCallback(this)
            event.cancel()
        }
    }

    override fun update(event: UpdateEvent) {
        event.ensurePreChild {
            val parent = parent()
            hovered = isTouching(event.updateContext.mousePos) && ((parent != null && parent.childAt(event.updateContext.mousePos) == this) || (parent == null))
        }
    }

    override fun draw(event: DrawEvent) {
        val matrices = event.drawContext
        renderer.draw(matrices, this, event.drawContext.delta)

        val textRenderer = MinecraftClient.getInstance().textRenderer
        val color = if (active) ARGB(255, 255, 255) else ARGB(160, 160, 160)

        if (textShadow) {
            Pencil.drawCenteredTextWithShadow(matrices, textRenderer, message, globalPos.x + size.x / 2, globalPos.y + (size.y - 8) / 2, color)
        } else {
            Pencil.drawText(event.drawContext, message, globalPos, 0.5f, color)
        }
    }

    override fun cursorStyle(): CursorStyle {
        return if (hovered) CursorStyle.HAND else CursorStyle.POINTER
    }

    fun interface Renderer {
        companion object {
            val VANILLA = Renderer { matrices, button, _ ->
                RenderSystem.enableDepthTest()
                val texture = if (button.active) if (button.hovered) HOVERED_TEXTURE else ACTIVE_TEXTURE else DISABLED_TEXTURE
                val sprite = org.teamvoided.voidlib.vui.VuiSpriteManager.getSprite(texture) ?: throw IllegalStateException("Sprite could not be found")
                RenderSystem.setShaderTexture(0, sprite.atlasId)
                Pencil.drawSprite(matrices, Vec3i(button.globalPos.x, button.globalPos.y, 0), button.size, sprite)
            }
        }

        fun flat(
            color: ARGB,
            hoveredColor: ARGB,
            disabledColor: ARGB
        ): Renderer {
            return Renderer { matrices: DrawContext, button: ButtonNode, _: Float ->
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
            return Renderer { matrices: DrawContext, button: ButtonNode, _: Float ->
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
                    texture,
                    button.globalPos,
                    Vec2f(uv.x, renderV),
                    button.size,
                    textureSize
                )
            }
        }

        fun draw(matrices: DrawContext, button: ButtonNode, delta: Float)
    }
}