package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import org.teamvoided.voidlib.core.LOGGER
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.ui.Event

class TextFieldWidgetNode() : Node() {
    var text: String = ""
    private val textFiled =
        TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 0, 30, Text.literal("name"))

    fun init() {
        textFiled.x = pos.x
        textFiled.y = pos.y
        textFiled.width = size.x
//        textFiled.height = pos.y + size.y
        textFiled.text = "Hi!"
        textFiled.setChangedListener { onChange(it) }
        textFiled.visible = true
        textFiled.active = true
        textFiled.isFocused = true
    }

    val onChangeCallback: Callback<TextFieldWidgetNode>
        get() = getCallbackAs(id("on_change_callback"))

    constructor(pos: Vec2i) : this() {
        this.pos = pos
        init()
    }

    constructor(pos: Vec2i, size: Vec2i) : this() {
        this.pos = pos
        this.size = size
        init()
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val context = event.drawContext
            textFiled.render(context.vanillaInstance, context.mousePos.x, context.mousePos.y, context.delta)
        }
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild { textFiled.tick() }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        LOGGER.info("MouseData - [{}] | [{}]", event, textFiled.mouseClicked(event.pos.x, event.pos.y, event.button))
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        LOGGER.info(
            "KeyData - [{}] | [{}]", event, textFiled.keyPressed(event.keyCode, event.scanCode, event.modifiers)
        )
    }

    private fun onChange(inText: String) {
        text = inText
        onChangeCallback(this)
    }
}