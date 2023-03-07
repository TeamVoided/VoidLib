package org.teamvoided.voidlib.vui.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.InputEvent
import org.teamvoided.voidlib.vui.UpdateContext
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.node.NodeIds.EXPANDABLE

open class ExpandableNode(override var name: String): Node() {
    var expanded = false
    protected var arrowPos = Vec2i(0, 0)
    protected var arrowSize = Vec2i(0, 0)
    protected val clickInterval = 0.2
    protected var interval = 0.0

    override var onMousePress: ((InputEvent.MousePressEvent) -> Unit)? = {
        if (isDoubleClick()) {
            expanded = !expanded
        } else if (isTouchingArrow(it.pos)) {
            expanded = !expanded
        } else if (isTouching(it.pos)) {
            interval = clickInterval
        }
    }

    override var updateCallback: ((UpdateContext) -> Unit)? = {
        arrowPos.x = ((pos.x + size.x) * 0.2).toInt()
        arrowPos.y = (arrowPos.x + pos.y) / 2
        arrowSize = globalPos - arrowPos

        if (interval != 0.0) interval -= it.delta
        if (interval <= 0) interval = 0.0
    }

    override var drawCallback: ((DrawContext) -> Unit)? = {
        RenderSystem.setShaderTexture(0, VuiSpriteManager.atlasId)
        if (expanded) {
            DrawableHelper.drawSprite(it.matrices, globalPos.x, globalPos.y, 0, arrowPos.x, arrowPos.y, VuiSpriteManager.getSprite(id("vres/expandable/open"))!!)
        } else {
            DrawableHelper.drawSprite(it.matrices, globalPos.x, globalPos.y, 0, arrowPos.x, arrowPos.y, VuiSpriteManager.getSprite(id("vres/expandable/closed"))!!)
        }
    }

    constructor(pos: Vec2i, name: String): this(name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, name: String): this(name) {
        this.pos = pos
        this.size = size
    }

    override fun draw(context: DrawContext) {
        drawCallback?.invoke(context)

        var previousY = 0

        if (expanded) {
            children().forEach {
                val originalPos = it.pos
                it.pos.x += 10
                it.pos.y += previousY
                previousY = it.pos.y
                it.draw(context)
                it.pos = originalPos
            }
        }
    }

    protected fun isTouchingArrow(point: Vec2i): Boolean {
        return point.x >= this.arrowPos.x && point.x <= this.arrowPos.x + arrowSize.x &&
                point.y >= this.arrowPos.y && point.y <= this.arrowPos.y + arrowSize.y
    }

    protected fun isDoubleClick(): Boolean {
        return interval > 0
    }

    override fun typeId(): Identifier = EXPANDABLE
}