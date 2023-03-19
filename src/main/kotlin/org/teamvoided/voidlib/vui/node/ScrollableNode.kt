package org.teamvoided.voidlib.vui.node

import net.minecraft.client.gui.DrawableHelper
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.UpdateContext
import org.teamvoided.voidlib.vui.node.NodeIds.SCROLLABLE

open class ScrollableNode(val scrollAxis: ScrollAxis = ScrollAxis.AXIS_Y, override var name: String): Node() {
    protected var step = 0
    protected var childrenTotalSize = Vec2i(0, 0)
    protected val highlightedColor = RGB(120, 120, 120).toInt()
    protected val unHighlightedColor = RGB(180, 180, 180).toInt()

    override var updateCallback: ((UpdateContext) -> Unit)? = {
        childrenTotalSize = Vec2i(0, 0)
        children().forEach { childrenTotalSize += it.size }
    }

    override var drawCallback: ((DrawContext) -> Unit)? = {
        if (isTouching(it.mousePos)) {
            when (scrollAxis) {
                ScrollAxis.AXIS_X -> {
                    DrawableHelper.fill(it.matrices, globalPos.x + step, globalPos.y, globalPos.x + size.x, globalPos.y + 10, unHighlightedColor)
                    DrawableHelper.fill(it.matrices, globalPos.x + step, globalPos.y, ((globalPos.x + size.x) * percentageScrolled()).toInt(), globalPos.y + 10, highlightedColor)
                } ScrollAxis.AXIS_Y -> {
                    DrawableHelper.fill(it.matrices, globalPos.x + size.x - 10, globalPos.y + step, globalPos.x + size.x, globalPos.y + size.y, unHighlightedColor)
                    DrawableHelper.fill(it.matrices, globalPos.x + size.x - 10, globalPos.y + step, globalPos.x + size.x, ((globalPos.y + size.y) * percentageScrolled()).toInt(), highlightedColor)
                }
            }
        }
    }

    constructor(pos: Vec2i, scrollAxis: ScrollAxis = ScrollAxis.AXIS_Y, name: String): this(scrollAxis, name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, scrollAxis: ScrollAxis = ScrollAxis.AXIS_Y, name: String): this(scrollAxis, name) {
        this.pos = pos
        this.size = size
    }

    override fun draw(context: DrawContext) {
        this.drawCallback?.invoke(context)

        DrawableHelper.enableScissor(globalPos.x, globalPos.y, globalPos.x + size.x, globalPos.y + size.y)
        children().forEach {
            val originalPos = it.pos.copy()
            when (scrollAxis) {
                ScrollAxis.AXIS_X -> { it.pos.x -= step }
                ScrollAxis.AXIS_Y -> { it.pos.y -= step }
            }
            it.draw(context)
            it.pos = originalPos
        }
        DrawableHelper.disableScissor()
    }

    override fun typeId(): Identifier = SCROLLABLE

    fun percentageScrolled(): Float {
        return when (scrollAxis) {
            ScrollAxis.AXIS_X -> {
                (childrenTotalSize.x - step) / childrenTotalSize.x.toFloat()
            } ScrollAxis.AXIS_Y -> {
                (childrenTotalSize.y - step) / childrenTotalSize.y.toFloat()
            }
        }
    }

    fun step() = step

    enum class ScrollAxis {
        AXIS_X,
        AXIS_Y
    }
}