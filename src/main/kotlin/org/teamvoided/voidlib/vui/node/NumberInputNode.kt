package org.teamvoided.voidlib.vui.node

import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.core.isNumeric
import org.teamvoided.voidlib.core.safeSubstring
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.InputEvent
import org.teamvoided.voidlib.vui.node.NodeIds.NUMBER_INPUT
import java.math.BigDecimal

open class NumberInputNode(override var name: String): Node() {
    protected var captureText = false
    protected var text = StringBuilder()
    protected var textPosition = 0
    protected val interval = 0.5f
    protected var tick = 0f

    override var onMousePress: ((InputEvent.MousePressEvent) -> Unit)? = {
        captureText = isTouching(it.pos)
    }

    override var onKeyPress: ((InputEvent.KeyPressEvent) -> Unit)? = {
        if (captureText) {
            if (it.keyCode == GLFW.GLFW_KEY_BACKSLASH) {
                val t1 = text.substring(0, textPosition).dropLast(1)
                val t2 = text.safeSubstring(textPosition + 1)
                text.replace(0, text.length - 1, "$t1$t2")
            }

            if (it.keyCode == GLFW.GLFW_KEY_DELETE) {
                val t1 = text.substring(0, textPosition).drop(1)
                val t2 = text.safeSubstring(textPosition + 1)
                text.replace(0, text.length - 1, "$t1$t2")
            }

            if (it.keyCode == GLFW.GLFW_KEY_LEFT) {
                if (textPosition > 0) textPosition -= 1
            }

            if (it.keyCode == GLFW.GLFW_KEY_RIGHT) {
                if (textPosition < text.length - 1) textPosition += 1
            }
        }
    }

    override var onCharTyped: ((InputEvent.CharTypedEvent) -> Unit)? = {
        val t1 = text.substring(0, textPosition)
        val t2 = text.safeSubstring(textPosition + 1)
        if (captureText && "$t1${it.char}$t2".isNumeric()) text = StringBuilder("$t1${it.char}$t2")
    }

    override var drawCallback: ((context: DrawContext) -> Unit)? = {
        tick += it.delta

        val textRenderer = MinecraftClient.getInstance().textRenderer

        if (tick >= interval) {
            textRenderer.draw(
                it.matrices,
                "${text.substring(0, textPosition)}|${text.safeSubstring(textPosition + 1)}",
                globalPos.x.toFloat(),
                (globalPos.y + size.y - 9) / 2f,
                RGB(255, 255, 255).toInt()
            )
            tick = 0f
        } else {
            textRenderer.draw(
                it.matrices,
                text.toString(),
                globalPos.x.toFloat(),
                (globalPos.y + size.y - 9) / 2f,
                RGB(255, 255, 255).toInt()
            )
        }
    }

    constructor(pos: Vec2i, name: String): this(name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, name: String): this(name) {
        this.pos = pos
        this.size = size
    }

    fun toNumber(): Number = BigDecimal(text.toString())

    override fun typeId(): Identifier = NUMBER_INPUT
}