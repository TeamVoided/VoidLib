package org.teamvoided.voidlib.vui.node

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.node.NodeIds.STRING

open class StringNode(var text: Text, var color: RGB, override var name: String): Node() {
    override var drawCallback: ((context: DrawContext) -> Unit)? = {
        DrawableHelper.drawCenteredText(
            it.matrices,
            MinecraftClient.getInstance().textRenderer,
            text,
            (globalPos.x + size.x) / 2,
            (globalPos.y + size.y) / 2,
            color.toInt()
        )
    }

    constructor(pos: Vec2i, text: Text, color: RGB, name: String): this(text, color, name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, text: Text, color: RGB, name: String): this(text, color, name) {
        this.pos = pos
        this.size = size
    }

    override fun typeId(): Identifier = STRING
}