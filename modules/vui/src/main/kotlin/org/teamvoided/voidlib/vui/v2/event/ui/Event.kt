package org.teamvoided.voidlib.vui.v2.event.ui

import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i

sealed class Event {
    private var canceled = false

    fun cancel() {
        canceled = true
    }

    fun canceled() = canceled

    sealed class InputEvent: Event() {
        data class MousePressEvent(val pos: Vec2d, val button: Int): InputEvent()
        data class MouseReleaseEvent(val pos: Vec2d, val button: Int): InputEvent()
        data class MouseScrollEvent(val pos: Vec2d, val amount: Double): InputEvent()
        data class MouseDragEvent(val pos: Vec2d, val delta: Vec2d, val button: Int): InputEvent()
        data class KeyPressEvent(val keyCode: Int, val scanCode: Int, val modifiers: Int): InputEvent()
        data class KeyReleaseEvent(val keyCode: Int, val scanCode: Int, val modifiers: Int): InputEvent()
        data class CharTypedEvent(val char: Char, val modifiers: Int): InputEvent()
    }

    sealed interface LogicalEventContext {
        data class DrawContext(
            val matrices: MatrixStack,
            val vanillaInstance: net.minecraft.client.gui.DrawContext,
            val mousePos: Vec2i,
            val partialTicks: Float,
            val delta: Float
        ): LogicalEventContext
        data class UpdateContext(val delta: Float, val mousePos: Vec2i): LogicalEventContext
    }

    sealed class LogicalEvent(val state: State): Event() {
        enum class State { PreChild, PostChild }

        class DrawEvent(val drawContext: LogicalEventContext.DrawContext, state: State): LogicalEvent(state)

        class UpdateEvent(val updateContext: LogicalEventContext.UpdateContext, state: State): LogicalEvent(state)

        fun ensurePreChild(action: () -> Unit) {
            if (state == State.PreChild)
                action()
        }

        fun ensurePostChild(action: () -> Unit) {
            if (state == State.PostChild)
                action()
        }
    }
}
