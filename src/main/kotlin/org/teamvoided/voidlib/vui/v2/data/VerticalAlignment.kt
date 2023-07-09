package org.teamvoided.voidlib.vui.v2.data

enum class VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM;

    fun align(componentWidth: Int, span: Int): Int {
        return when (this) {
            TOP -> 0
            CENTER -> span / 2 - componentWidth / 2
            BOTTOM -> span - componentWidth
        }
    }
}
