package org.teamvoided.voidlib.vui.v2.screen.cursor

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Window
import org.lwjgl.glfw.GLFW
import java.util.*
import java.util.function.Consumer

open class CursorAdapter protected constructor(protected val windowHandle: Long) {
    protected val cursors = EnumMap<CursorStyle, Long>(CursorStyle::class.java)
    protected var lastCursorStyle = CursorStyle.POINTER
    protected var disposed = false

    init {
        for (style in ACTIVE_STYLES) {
            cursors[style] = GLFW.glfwCreateStandardCursor(style.glfw)
        }
    }

    fun applyStyle(style: CursorStyle) {
        if (disposed || lastCursorStyle === style) return
        if (style === CursorStyle.NONE) {
            GLFW.glfwSetCursor(windowHandle, 0)
        } else {
            GLFW.glfwSetCursor(windowHandle, cursors[style]!!)
        }
        lastCursorStyle = style
    }

    fun dispose() {
        if (disposed) return
        cursors.values.forEach(Consumer { cursor: Long? ->
            GLFW.glfwDestroyCursor(
                cursor!!
            )
        })
        disposed = true
    }

    companion object {
        protected val ACTIVE_STYLES = arrayOf(CursorStyle.POINTER, CursorStyle.TEXT, CursorStyle.HAND, CursorStyle.MOVE)
        fun ofClientWindow(): CursorAdapter {
            return CursorAdapter(MinecraftClient.getInstance().window.handle)
        }

        fun ofWindow(window: Window): CursorAdapter {
            return CursorAdapter(window.handle)
        }

        fun ofWindow(windowHandle: Long): CursorAdapter {
            return CursorAdapter(windowHandle)
        }
    }
}
