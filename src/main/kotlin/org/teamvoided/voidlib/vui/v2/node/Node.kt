package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.nbt.NbtCompound
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent.*
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEvent.*
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.UpdateContext
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle
import java.util.*

abstract class Node() {
    private var parent: Node? = null
    private val children: MutableList<Node> = LinkedList<Node>()

    var globalPos: Vec2i
        get() = if (parent != null) (parent!!.globalPos + pos) else pos
        set(value) = if (parent != null) pos = (value - parent!!.pos)  else pos = value
    var pos = Vec2i(0, 0)
    var size = Vec2i(0, 0)

    protected constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    protected constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onMousePress(event: MousePressEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onMouseRelease(event: MouseReleaseEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onMouseScroll(event: MouseScrollEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onMouseDrag(event: MouseDragEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onKeyPress(event: KeyPressEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onKeyRelease(event: KeyReleaseEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns false if they should not be notified, will also cancel children added after this child
    */
    protected open fun onCharTyped(event: CharTypedEvent): Boolean { return true }

    protected open fun update(event: UpdateEvent) {}
    protected open fun draw(event: DrawEvent) {}

    open fun cursorStyle(): CursorStyle { return CursorStyle.NONE }

    fun inflate(size: Vec2i) {
        this.size = size
    }

    fun moveTo(pos: Vec2i) {
        this.pos = pos
    }

    fun moveToGlobal(globalPos: Vec2i) {
        this.globalPos = globalPos
    }

    fun childAt(pos: Vec2i): Node? {
        return childAt(pos.x, pos.y)
    }

    fun childAt(x: Int, y: Int): Node? {
        val iter: ListIterator<Node> = children().listIterator(children().size)
        while (iter.hasPrevious()) {
            val child: Node = iter.previous()
            if (child.isTouching(Vec2i(x, y))) {
                return if (child.hasChildren()) {
                    child.childAt(x, y) ?: child
                } else child
            }
        }
        return if (this.isTouching(Vec2i(x, y))) this else null
    }

    fun hasChildren() = children.isNotEmpty()

    fun mount(parent: Node?) {
        dismount()
        parent?.addChild(this)
    }

    fun dismount() {
        this.parent?.removeChild(this)
        this.parent = null
    }

    fun addChild(child: Node) {
        child.parent?.removeChild(child)
        children.add(child)

        val oldGlobal = child.globalPos
        child.parent = this
        child.globalPos = oldGlobal
    }

    fun removeChild(child: Node) {
        children.remove(child)

        val oldGlobal = child.globalPos

        child.parent = null
        child.globalPos = oldGlobal
    }

    fun isTouching(point: Vec2i): Boolean {
        return point.x >= this.globalPos.x && point.x <= this.globalPos.x + size.x &&
                point.y >= this.globalPos.y && point.y <= this.globalPos.y + size.y
    }

    fun isTouching(point: Vec2f): Boolean {
        return point.x >= this.globalPos.x && point.x <= this.globalPos.x + size.x &&
                point.y >= this.globalPos.y && point.y <= this.globalPos.y + size.y
    }

    fun isTouching(point: Vec2d): Boolean {
        return point.x >= this.globalPos.x && point.x <= this.globalPos.x + size.x &&
                point.y >= this.globalPos.y && point.y <= this.globalPos.y + size.y
    }

    fun dispatchInputEvent(event: InputEvent): Boolean {
        val updateChildren = when (event) {
            is MousePressEvent -> onMousePress(event)
            is MouseReleaseEvent -> onMouseRelease(event)
            is MouseScrollEvent -> onMouseScroll(event)
            is MouseDragEvent -> onMouseDrag(event)
            is KeyPressEvent -> onKeyPress(event)
            is KeyReleaseEvent -> onKeyRelease(event)
            is CharTypedEvent -> onCharTyped(event)
        }

        return if (updateChildren) {
            children.forEach {
                val b = it.dispatchInputEvent(event)
                if (!b) return false
            }; return true
        } else false
    }

    fun dispatchLogicalEvent(context: LogicalEventContext) {
        when (context) {
            is DrawContext -> draw(DrawEvent(context, State.PreChild))
            is UpdateContext -> update(UpdateEvent(context, State.PreChild))
        }

        children.forEach { it.dispatchLogicalEvent(context) }

        when (context) {
            is DrawContext -> draw(DrawEvent(context, State.PostChild))
            is UpdateContext -> update(UpdateEvent(context, State.PostChild))
        }
    }

    fun children(): List<Node> = children

    fun parent() = parent

    data class Type<T: Node>(val deserialize: (NbtCompound) -> T)
}