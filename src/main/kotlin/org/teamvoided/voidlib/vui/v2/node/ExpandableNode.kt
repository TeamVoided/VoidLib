package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.v2.event.Event

open class ExpandableNode(val arrowScale: Double) : Node() {
    var expanded = false
    protected var arrowPos = Vec2i(0, 0)
    protected var arrowSize = Vec2i(0, 0)
    protected val clickInterval = 0.2
    protected var interval = 0.0


    constructor(pos: Vec2i, arrowScale: Double) : this(arrowScale) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, arrowScale: Double) : this(arrowScale) {
        this.pos = pos
        this.size = size
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (isDoubleClick()) {
            expanded = !expanded
        } else if (isTouchingArrow(event.pos)) {
            expanded = !expanded
        } else if (isTouching(event.pos)) {
            interval = clickInterval
        }
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        arrowPos.x = (((globalPos.x + size.x) * 0.05) * arrowScale).toInt()
        arrowPos.y = ((globalPos.y + arrowPos.x) * arrowScale).toInt()
        arrowSize = globalPos - arrowPos

        if (interval != 0.0) interval -= event.updateContext.delta
        if (interval <= 0) interval = 0.0
    }


    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            RenderSystem.setShaderTexture(0, VuiSpriteManager.atlasId)
            if (expanded) {
                DrawableHelper.drawSprite(
                    event.drawContext.matrices,
                    globalPos.x,
                    globalPos.y,
                    0,
                    arrowPos.x,
                    arrowPos.y,
                    VuiSpriteManager.getSprite(id("vres/expandable/open"))!!
                )
            } else {
                DrawableHelper.drawSprite(
                    event.drawContext.matrices,
                    globalPos.x,
                    globalPos.y,
                    0,
                    arrowPos.x,
                    arrowPos.y,
                    VuiSpriteManager.getSprite(id("vres/expandable/closed"))!!
                )
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

}