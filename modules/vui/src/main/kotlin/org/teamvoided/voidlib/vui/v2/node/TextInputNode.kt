package org.teamvoided.voidlib.vui.v2.node


import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderLayer
import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Util
import net.minecraft.util.math.MathHelper
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import java.util.function.BiFunction
import kotlin.math.abs

class TextInputNode(val color: ARGB) : Node() {
    var active = true
    private var focusedTicks = 0
    private var selectionStart = 0
    private var selectionEnd = 0
    private var selecting = true
    private val editable = true
    private var firstCharacterIndex = 0

    var width = size.x
    var height = size.y

    private val renderTextProvider =
        BiFunction { string: String, integer: Int -> OrderedText.styledForwardsVisitedString(string, Style.EMPTY) }

    private val placeholder: Text? = null
    private val suggestion: String? = null

    private val editableColor = 14737632
    private val uneditableColor = 7368816

    private val textRenderer = MinecraftClient.getInstance().textRenderer
    var text: String = ""

    val onChangeCallback: Callback<TextInputNode>
        get() = getCallbackAs(id("on_change_callback"))

    constructor(pos: Vec2i, color: ARGB) : this(color) {
        this.pos = pos

    }

    constructor(pos: Vec2i, size: Vec2i, color: ARGB) : this(color) {
        this.pos = pos
        this.size = size
        this.width = size.x
        this.height = size.y
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val c = event.drawContext
            renderButton(c.vanillaInstance, c.mousePos.x, c.mousePos.y, c.delta)
        }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (isTouching(event.pos)) active = true

        mouseClicked(event.pos.x, event.pos.y, event.button)
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild { ++focusedTicks }
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        keyPressed(event.keyCode, event.scanCode, event.modifiers)
    }

    override fun onCharTyped(event: Event.InputEvent.CharTypedEvent) {
        charTyped(event.char, event.modifiers)
    }


    fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        return if (active) {
            val bl: Boolean = this.isTouching(Vec2d(d, e))
            if (bl) {
//                this.playDownSound(MinecraftClient.getInstance().soundManager)
                onClick(d, e)
                return true
            }
            false
        } else false
    }


    // Stolen
    fun setCText(string: String) {
        text = string
        this.setCursorToEnd()
        this.setSelectionEnd(selectionStart)
        onChanged(string)
    }

//    fun getText(): String {
//        return text
//    }

    fun getSelectedText(): String {
        val i = selectionStart.coerceAtMost(selectionEnd)
        val j = selectionStart.coerceAtLeast(selectionEnd)
        return text.substring(i, j)
    }

    fun setSelectionEnd(i: Int) {
        val j = text.length
        selectionEnd = MathHelper.clamp(i, 0, j)
        if (this.textRenderer != null) {
            if (this.firstCharacterIndex > j) {
                this.firstCharacterIndex = j
            }
            val k: Int = this.getInnerWidth()
            val string: String = this.textRenderer.trimToWidth(text.substring(this.firstCharacterIndex), k)
            val l: Int = string.length + this.firstCharacterIndex
            if (selectionEnd == this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.textRenderer.trimToWidth(text, k, true).length
            }
            if (selectionEnd > l) {
                this.firstCharacterIndex += selectionEnd - l
            } else if (selectionEnd <= this.firstCharacterIndex) {
                this.firstCharacterIndex -= this.firstCharacterIndex - selectionEnd
            }
            this.firstCharacterIndex = MathHelper.clamp(this.firstCharacterIndex, 0, j)
        }
    }

    fun write(string: String) {
        val i = selectionStart.coerceAtMost(selectionEnd)
        val j = selectionStart.coerceAtLeast(selectionEnd)
        val string2 = SharedConstants.stripInvalidChars(string)

        text = StringBuilder(text).replace(i, j, string2).toString()

        setSelectionStart(i + string2.length)
        setSelectionEnd(selectionStart)
        onChanged(text)
    }

    private fun onChanged(string: String) = onChangeCallback(this)

    private fun erase(i: Int) {
        if (Screen.hasControlDown())
            this.eraseWords(i)
        else
            this.eraseCharacters(i)

    }

    fun eraseWords(i: Int) {
        if (text.isNotEmpty()) {
            if (selectionEnd != selectionStart) write("")
            else this.eraseCharacters(this.getWordSkipPosition(i) - selectionStart)
        }
    }

    fun eraseCharacters(i: Int) {
        if (text.isNotEmpty()) {
            if (selectionEnd != selectionStart) {
                write("")
            } else {
                val j: Int = this.getCursorPosWithOffset(i)
                val k = Math.min(j, selectionStart)
                val l = Math.max(j, selectionStart)
                if (k != l) {
                    val string = java.lang.StringBuilder(text).delete(k, l).toString()
                    text = string
                    setCursor(k)
                }
            }
        }
    }

    fun getWordSkipPosition(i: Int): Int = this.getWordSkipPosition(i, this.getCursor())
    private fun getWordSkipPosition(i: Int, j: Int): Int = this.getWordSkipPosition(i, j, true)
    private fun getWordSkipPosition(i: Int, j: Int, bl: Boolean): Int {
        var k = j
        val bl2 = i < 0
        val l = abs(i)
        for (m in 0 until l) {
            if (!bl2) {
                val n = text.length
                k = text.indexOf(32.toChar(), k)
                if (k == -1) k = n
                else while (bl && k < n && text[k] == ' ') ++k
            } else {
                while (bl && k > 0 && text[k - 1] == ' ') --k
                while (k > 0 && text[k - 1] != ' ') --k
            }
        }
        return k
    }

    fun moveCursor(i: Int) {
        setCursor(getCursorPosWithOffset(i))
    }

    private fun getCursorPosWithOffset(i: Int): Int = Util.moveCursor(text, selectionStart, i)

    fun setCursor(i: Int) {
        this.setSelectionStart(i)
        if (!this.selecting) {
            this.setSelectionEnd(selectionStart)
        }
        this.onChanged(text)
    }

    fun setSelectionStart(i: Int) {
        selectionStart = MathHelper.clamp(i, 0, text.length)
    }

    fun setCursorToStart() {
        setCursor(0)
    }

    fun setCursorToEnd() = this.setCursor(text.length)

    fun keyPressed(i: Int, j: Int, k: Int): Boolean {
        return if (!active) {
            false
        }
        else {
            selecting = Screen.hasShiftDown()

            if (Screen.isSelectAll(i)) {
                setCursorToEnd()
                setSelectionEnd(0)
                true
            } else if (Screen.isCopy(i)) {
                MinecraftClient.getInstance().keyboard.clipboard = this.getSelectedText()
                true
            } else if (Screen.isPaste(i)) {
                if (this.editable) {
                    write(MinecraftClient.getInstance().keyboard.clipboard)
                }
                true
            } else if (Screen.isCut(i)) {
                MinecraftClient.getInstance().keyboard.clipboard = this.getSelectedText()
                if (this.editable) {
                    write("")
                }
                true
            } else {
                when (i) {
                    259 -> {
                        if (this.editable) {
                            selecting = false
                            erase(-1)
                            selecting = Screen.hasShiftDown()
                        }
                        true
                    }

                    260, 264, 265, 266, 267 -> false
                    261 -> {
                        if (this.editable) {
                            selecting = false
                            erase(1)
                            selecting = Screen.hasShiftDown()
                        }
                        true
                    }

                    262 -> {
                        if (Screen.hasControlDown()) {
                            setCursor(this.getWordSkipPosition(1))
                        } else {
                            moveCursor(1)
                        }
                        true
                    }

                    263 -> {
                        if (Screen.hasControlDown()) {
                            setCursor(this.getWordSkipPosition(-1))
                        } else {
                            moveCursor(-1)
                        }
                        true
                    }

                    268 -> {
                        setCursorToStart()
                        true
                    }

                    269 -> {
                        setCursorToEnd()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    fun isActive(): Boolean = this.active

    fun charTyped(c: Char, i: Int): Boolean {
        return if (!isActive()) false
        else if (SharedConstants.isValidChar(c)) {
            if (editable) write(c.toString())
            true
        } else false
    }


    fun onClick(d: Double, e: Double) {
        var i: Int = MathHelper.floor(d) - this.globalPos.x
        val testValue = false
        if (testValue == false && false && testValue) i -= 4 // Draw bg

        val string = textRenderer.trimToWidth(text.substring(firstCharacterIndex), getInnerWidth())
        setCursor(textRenderer.trimToWidth(string, i).length + firstCharacterIndex)
    }


    fun renderButton(drawContext: DrawContext, i: Int, j: Int, f: Float) {
        if (active) {
            if (false) {
                /*
                val k = if (this.isFocused()) -1 else -6250336
                drawContext.fill(
                    size.x - 1,
                    size.y - 1,
                    size.x + this.width + 1,
                    size.y + this.height + 1,
                    k
                )
                drawContext.fill(
                    size.x,
                    size.y,
                    size.x + this.width,
                    size.y + this.height,
                    -16777216
                )
                 */
            }

            val k: Int = if (editable) this.editableColor else this.uneditableColor
            val l = selectionStart - firstCharacterIndex
            var m = selectionEnd - firstCharacterIndex
            val string = textRenderer.trimToWidth(text.substring(firstCharacterIndex), getInnerWidth())
            val bl = l >= 0 && l <= string.length
            val bl2 = this.active && focusedTicks / 32 % 2 == 0 && bl
            val n: Int = if (false) size.x + 4 else globalPos.x
            val o: Int = if (false) size.y + (this.height - 8) / 2 else globalPos.y
            var p = n
            if (m > string.length) m = string.length


            if (string.isNotEmpty()) {
                val string2 = if (bl) string.substring(0, l) else string
                p = drawContext.drawTextWithShadow(
                    textRenderer,
                    this.renderTextProvider.apply(string2, firstCharacterIndex) as OrderedText,
                    n,
                    o,
                    k
                )
            }
//            val bl3 = selectionStart < text.length || text.length >= this.getMaxLength()
            var q = p
            if (!bl) {
                q = if (l > 0) n + this.width else n
            }
//            else if (bl3) {
//                q = p - 1
//                --p
//            }
            if (string.isNotEmpty() && bl && l < string.length) {
                drawContext.drawTextWithShadow(
                    textRenderer, this.renderTextProvider.apply(
                        string.substring(l),
                        selectionStart
                    ) as OrderedText, p, o, k
                )
            }
            if (this.placeholder != null && string.isEmpty() && !this.active) {
                drawContext.drawTextWithShadow(textRenderer, this.placeholder, p, o, k)
            }

//            !bl3 && this.suggestion != null
            if (this.suggestion != null) {
                drawContext.drawTextWithShadow(textRenderer, this.suggestion, q - 1, o, -8355712)
            }
            if (bl2) {
//                if (bl3) {
//                    drawContext.fill(RenderLayer.getGuiOverlay(), q, o - 1, q + 1, o + 1 + 9, -3092272)
//                } else {
                drawContext.drawTextWithShadow(textRenderer, "_", q, o, k)
//                }
            }
            if (m != l) {
                val r = n + textRenderer.getWidth(string.substring(0, m))
                this.drawSelectionHighlight(drawContext, q, o - 1, r - 1, o + 1 + 9)
            }
        }
    }

    private fun drawSelectionHighlight(drawContext: DrawContext, i: Int, j: Int, k: Int, l: Int) {
        var i = i
        var j = j
        var k = k
        var l = l
        if (i < k) {
            val m = i
            i = k
            k = m
        }
        if (j < l) {
            val m = j
            j = l
            l = m
        }
        if (k > size.x + width) {
            k = size.x + width
        }
        if (i > size.x + width) {
            i = size.x + width
        }
        drawContext.fill(RenderLayer.getGuiTextHighlight(), i, j, k, l, -16776961)
    }

    fun getCursor(): Int = selectionStart


    fun getInnerWidth(): Int = size.y - 5


}