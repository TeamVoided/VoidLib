package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.gui.widget.SliderWidget
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.str
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import java.math.BigDecimal
import java.text.DecimalFormat

open class SliderNode(): Node() {
    protected var slider: VoidSlider? = null
    protected var isMouseOver = false

    var scrollStep = 0.05

    open var messageProvider: (String) -> Text = {
        val format = DecimalFormat("0.000")
        val formatted = format.format(BigDecimal(it))

        Text.literal(formatted)
    }

    private var aVal = 0.0

    var value
        get() = aVal
        set(value) = run {
            val v = MathHelper.clamp(value, 0.0, 1.0)

            if (this.value != v) {
                this.aVal = v

                slider?.void_updateMessage()
                slider?.void_applyValue()
            }
        }

    constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    protected open fun createSlider(pos: Vec2i, size: Vec2i): VoidSlider = VoidSlider(pos, size, this)

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        if (slider == null) slider = createSlider(pos, size)

        if (slider?.x != this.pos.x) slider?.x = this.pos.x
        if (slider?.y != this.pos.y) slider?.y = this.pos.y
        if (slider?.width != this.size.x) slider?.x = this.size.x
        if (slider?.height != this.size.y) slider?.y = this.size.y

        slider?.isFocused = parent()?.childAt(event.updateContext.mousePos) == this
        isMouseOver = isTouching(event.updateContext.mousePos)
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        slider?.render(event.drawContext.vanillaInstance, event.drawContext.mousePos.x, event.drawContext.mousePos.y, event.drawContext.delta)
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (isMouseOver) {
            slider?.onClick(event.pos.x, event.pos.y)
            event.cancel()
        }
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        if (slider?.keyPressed(event.keyCode, event.scanCode, event.modifiers) != false) {
            event.cancel()
        }
    }

    override fun onMouseDrag(event: Event.InputEvent.MouseDragEvent) {
        if (isMouseOver) {
            slider?.void_onDrag(event.pos, event.delta)
            event.cancel()
        }
    }

    override fun onMouseScroll(event: Event.InputEvent.MouseScrollEvent) {
        if (isMouseOver) {
            slider?.void_setValue(this.value + (this.scrollStep * event.amount))
            event.cancel()
        }
    }

    open class VoidSlider(val pos: Vec2i, val size: Vec2i, val node: SliderNode): SliderWidget(pos.x, pos.y, size.x, size.y, Text.literal("0.0"), 0.0) {
        fun void_updateMessage() = updateMessage()
        fun void_applyValue() = applyValue()
        fun void_onDrag(mousePos: Vec2d, delta: Vec2d) = onDrag(mousePos.x, mousePos.y, delta.x, delta.y)

        fun void_setValue(value: Double) {
            this.value = MathHelper.clamp(value, 0.0, 1.0)
            updateMessage()
            applyValue()
        }

        override fun updateMessage() {
            message = node.messageProvider(value.str)
        }

        override fun applyValue() {
            node.value = this.value
        }
    }
}