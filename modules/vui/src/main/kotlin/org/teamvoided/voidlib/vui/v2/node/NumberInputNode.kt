package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.isNumeric
import org.teamvoided.voidlib.core.safeSubstring
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent.*
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
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
        val touching = isTouching(event.pos)
        if (touching) event.cancel()
        captureText = touching
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

            event.cancel()
        }
    }

    override fun onCharTyped(event: CharTypedEvent) {
        val t1 = text.substring(0, textPosition)
        val t2 = text.safeSubstring(textPosition + 1)
        if (captureText && "$t1${event.char}$t2".isNumeric()) {
            text = StringBuilder("$t1${event.char}$t2")
            event.cancel()
        }
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            tick += event.drawContext.delta

            if (tick >= interval) {
                Pencil.drawText(
                    event.drawContext,
                    Text.literal("${text.substring(0, textPosition)}|${text.safeSubstring(textPosition + 1)}"),
                    globalPos,
                    0.5f,
                    ARGB(255, 255, 255, 255)
                )
                tick = 0f
            } else {
                Pencil.drawText(
                    event.drawContext,
                    Text.literal(text.toString()),
                    globalPos,
                    0.5f,
                    ARGB(255, 255, 255, 255)
                )
            }
        }
    }

    fun toNumber(): Number = BigDecimal(text.toString())
}