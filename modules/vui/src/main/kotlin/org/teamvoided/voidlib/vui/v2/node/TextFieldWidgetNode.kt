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
        TextFieldWidget(
            MinecraftClient.getInstance().textRenderer,
            200, 75, 200, 48,
//            pos.x, pos.y, pos.x + size.x, pos.y + size.y,
            Text.literal("name")
        )

    init {

        textFiled.text = "Hi!"
        textFiled.setChangedListener { onChange(it) }
    }

    val onChangeCallback: Callback<TextFieldWidgetNode>
        get() = getCallbackAs(id("on_change_callback"))

    constructor(pos: Vec2i) : this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i) : this() {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val context = event.drawContext
            textFiled.render(context.vanillaInstance, context.mousePos.x, context.mousePos.y, context.delta)
            textFiled.tick()
        }
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild {  }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (isTouching(event.pos)) LOGGER.info("yes")
        textFiled.mouseClicked(event.pos.x, event.pos.y, event.button)
    }

    override fun onKeyPress(event: Event.InputEvent.KeyPressEvent) {
        textFiled.keyPressed(event.keyCode, event.scanCode, event.modifiers)
    }

    fun onChange(inText: String) {
        text = inText
        onChangeCallback(this)
    }
}