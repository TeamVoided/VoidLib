package org.teamvoided.voidlib.vui.v2.screen.cursor

import org.lwjgl.glfw.GLFW

enum class CursorStyle(val glfw: Int) {
    /**
     * The default cursor style defined by
     * the operating system
     */
    NONE(0),

    /**
     * The default arrow-style pointing cursor
     */
    POINTER(GLFW.GLFW_ARROW_CURSOR),

    /**
     * The text selection, usually I-beam, cursor
     */
    TEXT(GLFW.GLFW_IBEAM_CURSOR),

    /**
     * The hand cursor which signals clickable areas
     */
    HAND(GLFW.GLFW_HAND_CURSOR),

    /**
     * The cross-shaped cursor which signals
     * draggable/movable areas
     */
    MOVE(GLFW.GLFW_RESIZE_ALL_CURSOR)
}
