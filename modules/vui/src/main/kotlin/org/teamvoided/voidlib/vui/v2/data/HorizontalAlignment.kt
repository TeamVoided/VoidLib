package org.teamvoided.voidlib.vui.v2.data

enum class HorizontalAlignment {
    LEFT,
    CENTER,
    RIGHT;

    fun align(componentWidth: Int, span: Int): Int {
        return when (this) {
            LEFT -> 0
            CENTER -> span / 2 - componentWidth / 2
            RIGHT -> span - componentWidth
        }
    }
}
