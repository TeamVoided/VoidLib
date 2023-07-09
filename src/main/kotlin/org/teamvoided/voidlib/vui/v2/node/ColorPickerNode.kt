package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.math.MathHelper
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.vui.impl.Vui
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle
import kotlin.math.min

open class ColorPickerNode(): Node() {
    protected var hue = .5f
    protected var saturation = 1f
    protected var value = 1f
    protected var alpha = 1f

    var color = ARGB.fromHSV(hue, saturation, value, alpha)

    var selectorWidth = 20
    var selectorPadding = 10
    var showAlpha = false

    private var hovered = false

    constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    override fun update(event: Event.LogicalEvent.UpdateEvent) {
        event.ensurePreChild {
            val parent = parent()
            hovered = isTouching(event.updateContext.mousePos) && ((parent != null && parent.childAt(event.updateContext.mousePos) == this) || (parent == null))
        }
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        val buffer = Tessellator.getInstance().buffer
        val matrices = event.drawContext.matrices
        val matrix = matrices.peek().positionMatrix

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex(matrix, renderX().f, renderY().f, 0f)
            .color(hue, 0f, 1f, 1f).next()
        buffer.vertex(matrix, renderX().f, (renderY() + renderHeight()).f, 0f)
            .color(hue, 0f, 0f, 1f).next()
        buffer.vertex(matrix, (renderX() + colorAreaWidth()).f, (renderY() + renderHeight()).f, 0f)
            .color(hue, 1f, 0f, 1f).next()
        buffer.vertex(matrix, (renderX() + colorAreaWidth()).f, renderY().f, 0f)
            .color(hue, 1f, 1f, 1f).next()

        Vui.hsvProgram.use()
        Tessellator.getInstance().draw()

        Pencil.drawRectOutline(
            matrices,
            Vec2i((renderX() + (saturation * colorAreaWidth()) - 1).i, (renderY() + ((1 - value) * (renderHeight() - 1)) - 1).i),
            Vec2i(3, 3),
            ARGB(255, 255, 255)
        )

        Pencil.drawSpectrum(matrices, Vec2f(renderX().f + hueSelectorX(), renderY().f), Vec2f(selectorWidth.f, renderHeight().f), true)
        Pencil.drawRectOutline(
            matrices,
            Vec2i(renderX() + hueSelectorX() - 1, renderY() + ((renderHeight() - 1) * (1 - hue) - 1).i),
            Vec2i(selectorWidth + 2, 3),
            ARGB(255, 255, 255)
        )

        if (showAlpha) {
            val color = ARGB.fromArgbInt(0xFF shl 24 or this.color.toInt())
            val zero = ARGB.fromArgbInt(0)
            Pencil.drawGradientRect(
                matrices,
                Vec2f(renderX() + alphaSelectorX().f, renderY().f),
                Vec2f(selectorWidth.f, renderHeight().f),
                color, color, zero, zero
            )
            Pencil.drawRectOutline(
                matrices,
                Vec2i(renderX() + alphaSelectorX() - 1, renderY() + ((renderHeight() - 1) * (1 - alpha) - 1).i),
                Vec2i(selectorWidth + 2, 3),
                ARGB(255, 255, 255)
            )
        }
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (hovered) {
            updateFromMouse(event.pos)
            event.cancel()
        }
    }

    override fun onMouseDrag(event: Event.InputEvent.MouseDragEvent) {
        if (hovered) {
            updateFromMouse(event.pos + event.delta)
            event.cancel()
        }
    }

    override fun cursorStyle(): CursorStyle {
        return if (hovered) CursorStyle.HAND else CursorStyle.POINTER
    }

    fun updateFromMouse(mousePos: Vec2d) {
        var mouseX = mousePos.x - globalPos.x
        var mouseY = mousePos.y - globalPos.y

        mouseX = MathHelper.clamp(mouseX - 1, 0.0, renderWidth().d)
        mouseY = MathHelper.clamp(mouseY - 1, 0.0, renderHeight().d)
        if (showAlpha && mouseX >= alphaSelectorX()) {
            alpha = 1 - (mouseY / renderHeight()).f
        } else if (mouseX >= hueSelectorX()) {
            hue = 1 - (mouseY / renderHeight()).f
        } else {
            saturation = min(1.0, (mouseX / colorAreaWidth()).d).f
            value = 1 - (mouseY / renderHeight()).f
        }
        this.color = ARGB.fromHSV(hue, saturation, value, alpha)
    }

    protected fun renderX(): Int {
        return this.globalPos.x + 1
    }

    protected fun renderY(): Int {
        return this.globalPos.y + 1
    }

    protected fun renderWidth(): Int {
        return this.size.x - 2
    }

    protected fun renderHeight(): Int {
        return this.size.y - 2
    }

    protected fun colorAreaWidth(): Int {
        return if (showAlpha) renderWidth() - selectorPadding - selectorWidth - selectorPadding - selectorWidth else renderWidth() - selectorPadding - selectorWidth
    }

    protected fun hueSelectorX(): Int {
        return if (showAlpha) renderWidth() - selectorWidth - selectorPadding - selectorWidth else renderWidth() - selectorWidth
    }

    protected fun alphaSelectorX(): Int {
        return renderWidth() - selectorWidth
    }
}