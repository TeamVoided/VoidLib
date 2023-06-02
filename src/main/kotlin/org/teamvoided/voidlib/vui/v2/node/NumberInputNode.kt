package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.core.isNumeric
import org.teamvoided.voidlib.core.safeSubstring
import org.teamvoided.voidlib.vui.v2.event.Event
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent.*
import java.math.BigDecimal

open class NumberInputNode() : Node() {
    protected var captureText = false
    protected var text = StringBuilder()
    protected var textPosition = 0
    protected val interval = 0.5f
    protected var tick = 0f

    constructor(pos: Vec2i) : this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i) : this() {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: MousePressEvent) {
        captureText = isTouching(event.pos)
    }

    override fun onKeyPress(event: KeyPressEvent) {
        if (captureText) {
            if (event.keyCode == GLFW.GLFW_KEY_BACKSLASH) {
                val t1 = text.substring(0, textPosition).dropLast(1)
                val t2 = text.safeSubstring(textPosition + 1)
                text.replace(0, text.length - 1, "$t1$t2")
            }

            if (event.keyCode == GLFW.GLFW_KEY_DELETE) {
                val t1 = text.substring(0, textPosition).drop(1)
                val t2 = text.safeSubstring(textPosition + 1)
                text.replace(0, text.length - 1, "$t1$t2")
            }

            if (event.keyCode == GLFW.GLFW_KEY_LEFT) {
                if (textPosition > 0) textPosition -= 1
            }

            if (event.keyCode == GLFW.GLFW_KEY_RIGHT) {
                if (textPosition < text.length - 1) textPosition += 1
            }
        }
    }

    override fun onCharTyped(event: CharTypedEvent) {
        val t1 = text.substring(0, textPosition)
        val t2 = text.safeSubstring(textPosition + 1)
        if (captureText && "$t1${event.char}$t2".isNumeric()) text = StringBuilder("$t1${event.char}$t2")
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            tick += event.drawContext.delta

            val textRenderer = MinecraftClient.getInstance().textRenderer
            val matrices = event.drawContext.matrices

            if (tick >= interval) {
                textRenderer.draw(
                    matrices,
                    "${text.substring(0, textPosition)}|${text.safeSubstring(textPosition + 1)}",
                    globalPos.x.toFloat(),
                    (globalPos.y + size.y - 9) / 2f,
                    RGB(255, 255, 255).toInt()
                )
                tick = 0f
            } else {
                textRenderer.draw(
                    matrices,
                    text.toString(),
                    globalPos.x.toFloat(),
                    (globalPos.y + size.y - 9) / 2f,
                    RGB(255, 255, 255).toInt()
                )
            }
        }
    }

    fun toNumber(): Number = BigDecimal(text.toString())

}