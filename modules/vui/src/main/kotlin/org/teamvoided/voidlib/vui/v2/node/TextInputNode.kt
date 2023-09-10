package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import java.util.function.Consumer

class TextInputNode(val color: ARGB) : Node() {
    var active = false
    var text: String = ""

    val onChangeCallback: Callback<TextInputNode>
        get() = getCallbackAs(id("on_change_callback"))

    constructor(pos: Vec2i, color: ARGB, onChange: Consumer<String>) : this(color) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, color: ARGB, onChange: Consumer<String>) : this(color) {
        this.pos = pos
        this.size = size
    }


    private fun onChanged() = onChangeCallback(this)

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (isTouching(event.pos)) {
            active = true
        }
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        if (!active) return


        /*

            this.selecting = Screen.hasShiftDown()

            if (Screen.isSelectAll(i)) {
                this.setCursorToEnd()
                this.setSelectionEnd(0)
                true
            } else if (Screen.isCopy(i)) {
                MinecraftClient.getInstance().keyboard.clipboard = this.getSelectedText()
                true
            } else if (Screen.isPaste(i)) {
                if (this.editable) {
                    this.write(MinecraftClient.getInstance().keyboard.clipboard)
                }
                true
            } else if (Screen.isCut(i)) {
                MinecraftClient.getInstance().keyboard.clipboard = this.getSelectedText()
                if (this.editable) {
                    this.write("")
                }
                true
            } else {
                when (i) {
                    259 -> {
                        if (this.editable) {
                            this.selecting = false
                            this.erase(-1)
                            this.selecting = Screen.hasShiftDown()
                        }
                        true
                    }

                    260, 264, 265, 266, 267 -> false
                    261 -> {
                        if (this.editable) {
                            this.selecting = false
                            this.erase(1)
                            this.selecting = Screen.hasShiftDown()
                        }
                        true
                    }

                    262 -> {
                        if (Screen.hasControlDown()) {
                            this.setCursor(this.getWordSkipPosition(1))
                        } else {
                            this.moveCursor(1)
                        }
                        true
                    }

                    263 -> {
                        if (Screen.hasControlDown()) {
                            this.setCursor(this.getWordSkipPosition(-1))
                        } else {
                            this.moveCursor(-1)
                        }
                        true
                    }

                    268 -> {
                        this.setCursorToStart()
                        true
                    }

                    269 -> {
                        this.setCursorToEnd()
                        true
                    }

                    else -> false
                }
            }
        */
        val char = ""

        /*
        val i: Int = Math.min(this.selectionStart, this.selectionEnd)
        val j: Int = Math.max(this.selectionStart, this.selectionEnd)
        val k: Int = this.maxLength - text.length - (i - j)
        var string2 = SharedConstants.stripInvalidChars(string)
        var l = string2.length
        if (k < l) {
            string2 = string2.substring(0, k)
            l = k
        }

        val string3 = StringBuilder(text).replace(i, j, string2).toString()
        if (this.textPredicate.test(string3)) {
            text = string3
            this.setSelectionStart(i + l)
            this.setSelectionEnd(this.selectionStart)
            onChanged(text)
        }
         */

        val string2 = SharedConstants.stripInvalidChars(char)
        text = string2
        onChanged()
    }

    /*

    fun hasControlDown(): Boolean {
        return if (MinecraftClient.IS_SYSTEM_MAC) {
            (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 343)
                    || InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 347))
        } else {
            (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 341)
                    || InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 345))
        }
    }

    fun hasShiftDown(): Boolean {
        return (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 340)
                || InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 344))
    }

    fun hasAltDown(): Boolean {
        return (InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 342)
                || InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, 346))
    }



    private fun erase(i: Int) {
        if (Screen.hasControlDown()) eraseWords(i)
        else eraseCharacters(i)
    }

    fun eraseWords(i: Int) {
        if (text.isNotEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("")
            } else {
                eraseCharacters(this.getWordSkipPosition(i) - this.selectionStart)
            }
        }
    }

    fun eraseCharacters(i: Int) {
        if (!text.isEmpty()) {
            if (this.selectionEnd != this.selectionStart) {
                this.write("")
            } else {
                val j: Int = this.getCursorPosWithOffset(i)
                val k = Math.min(j, this.selectionStart)
                val l = Math.max(j, this.selectionStart)
                if (k != l) {
                    val string = StringBuilder(text).delete(k, l).toString()
                    if (this.textPredicate.test(string)) {
                        text = string
                        this.setCursor(k)
                    }
                }
            }
        }
    }
     */

}