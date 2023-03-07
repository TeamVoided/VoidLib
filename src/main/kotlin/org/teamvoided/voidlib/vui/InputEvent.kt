package org.teamvoided.voidlib.vui

import org.teamvoided.voidlib.core.datastructures.Vec2i

sealed class InputEvent {
    data class MousePressEvent(val pos: Vec2i, val button: Int): InputEvent()
    data class MouseReleaseEvent(val pos: Vec2i, val button: Int): InputEvent()
    data class MouseScrollEvent(val pos: Vec2i, val amount: Double): InputEvent()
    data class MouseDragEvent(val pos: Vec2i, val delta: Vec2i, val button: Int): InputEvent()

    data class KeyPressEvent(val keyCode: Int, val scanCode: Int, val modifiers: Int): InputEvent()
    data class KeyReleaseEvent(val keyCode: Int, val scanCode: Int, val modifiers: Int): InputEvent()

    data class CharTypedEvent(val char: Char, val modifiers: Int): InputEvent()
}