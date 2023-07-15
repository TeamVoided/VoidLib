package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.datastructures.vector.Vec3i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.node.layout.BoundingBox
import org.teamvoided.voidlib.vui.v2.node.layout.FlowLayout
import org.teamvoided.voidlib.vui.v2.rendering.Pencil

class DropdownNode(): FlowLayout() {
    companion object {
        val OPEN_SPRITE = id("vres/expandable/open")
        val CLOSED_SPRITE = id("vres/expandable/closed")
    }

    var expanded = false
    var openWhenHovered = false
    var arrowScale = Vec2d(0.05, 0.75)

    private var arrowPos = Vec2i(0, 0)
    private var arrowSize = Vec2i(0, 0)
    private val arrowBoundingBox = BoundingBox.of(arrowPos, arrowSize)

    constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild {
            arrowPos = (this.globalPos.to2d() + size.to2d() * arrowScale).to2i()
            arrowSize = (size.to2d() * arrowScale).to2i()
            arrowBoundingBox.inflate(arrowPos, arrowSize)

            if (openWhenHovered) {
                expanded = isTouching(event.updateContext.mousePos)
            }
        }
    }

    override fun onMouseRelease(event: Event.InputEvent.MouseReleaseEvent) {
        if (arrowBoundingBox.isTouching(event.pos)) {
            expanded = !expanded
            event.cancel()
        }
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            RenderSystem.setShaderTexture(0, org.teamvoided.voidlib.vui.VuiSpriteManager.atlasId)
            if (expanded) {
                Pencil.drawSprite(event.drawContext, Vec3i(arrowPos.x, arrowPos.y, 0), arrowSize, org.teamvoided.voidlib.vui.VuiSpriteManager.getSprite(
                    OPEN_SPRITE
                ) ?: throw IllegalStateException("Could not find core sprite $OPEN_SPRITE"))
            } else {
                Pencil.drawSprite(event.drawContext, Vec3i(arrowPos.x, arrowPos.y, 0), arrowSize, org.teamvoided.voidlib.vui.VuiSpriteManager.getSprite(
                    CLOSED_SPRITE
                ) ?: throw IllegalStateException("Could not find core sprite $CLOSED_SPRITE"))
                event.cancel()
            }
        }
    }
}