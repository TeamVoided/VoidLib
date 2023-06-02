package org.teamvoided.voidlib.vui.v2.screen

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEventContext.UpdateContext
import org.teamvoided.voidlib.vui.v2.node.Node

abstract class VuiScreen(title: Text): Screen(title) {
    abstract val parent: Node
    protected var oldScaleFactor = 1.0

    open fun vuiInit() {}

    override fun init() {
        val window = MinecraftClient.getInstance().window
        oldScaleFactor = window.scaleFactor
        window.scaleFactor = 3.0
        vuiInit()
        super.init()
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val delta = MinecraftClient.getInstance().lastFrameDuration
        parent.dispatchLogicalEvent(UpdateContext(delta, Vec2i(mouseX, mouseY)))
        parent.dispatchLogicalEvent(DrawContext(matrices, Vec2i(mouseX, mouseY), partialTicks, delta, false))
        parent.dispatchLogicalEvent(DrawContext(matrices, Vec2i(mouseX, mouseY), partialTicks, delta, true))
        super.render(matrices, mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        parent.dispatchInputEvent(InputEvent.MousePressEvent(Vec2i(mouseX.toInt(), mouseY.toInt()), button))
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        parent.dispatchInputEvent(InputEvent.MouseReleaseEvent(Vec2i(mouseX.toInt(), mouseY.toInt()), button))
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        parent.dispatchInputEvent(InputEvent.MouseScrollEvent(Vec2i(mouseX.toInt(), mouseY.toInt()), amount))
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        parent.dispatchInputEvent(InputEvent.MouseDragEvent(Vec2i(mouseX.toInt(), mouseY.toInt()), Vec2i(deltaX.toInt(), deltaY.toInt()), button))
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        parent.dispatchInputEvent(InputEvent.KeyPressEvent(keyCode, scanCode, modifiers))
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        parent.dispatchInputEvent(InputEvent.KeyReleaseEvent(keyCode, scanCode, modifiers))
        return super.keyReleased(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        parent.dispatchInputEvent(InputEvent.CharTypedEvent(chr, modifiers))
        return super.charTyped(chr, modifiers)
    }

    override fun close() {
        MinecraftClient.getInstance().window.scaleFactor = oldScaleFactor
        super.close()
    }
}