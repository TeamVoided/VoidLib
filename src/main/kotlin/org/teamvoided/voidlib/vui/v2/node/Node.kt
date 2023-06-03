package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.nbt.NbtCompound
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent
import org.teamvoided.voidlib.vui.v2.event.Event.InputEvent.*
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEvent.*
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEventContext
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEventContext.UpdateContext
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
    * Returns False if they should not be notified
    */
    protected open fun onMousePress(event: MousePressEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onMouseRelease(event: MouseReleaseEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onMouseScroll(event: MouseScrollEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onMouseDrag(event: MouseDragEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onKeyPress(event: KeyPressEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onKeyRelease(event: KeyReleaseEvent): Boolean { return true }

    /*
    * Returns true if children should be notified of the event
    * Returns False if they should not be notified
    */
    protected open fun onCharTyped(event: CharTypedEvent): Boolean { return true }

    protected open fun update(event: UpdateEvent) {}
    protected open fun draw(event: DrawEvent) {}

    fun addChild(child: Node) {
        children.add(child)

        val oldGlobal = child.globalPos

        child.parent = this
        child.globalPos = oldGlobal
    }

    fun removeChild(child: Node) {
        children.remove(child)

        val oldGlobal = child.globalPos

        child.parent = this
        child.globalPos = oldGlobal
    }

    fun isTouching(point: Vec2i): Boolean {
        return point.x >= this.globalPos.x && point.x <= this.globalPos.x + size.x &&
                point.y >= this.globalPos.y && point.y <= this.globalPos.y + size.y
    }

    fun dispatchInputEvent(event: InputEvent) {
        val updateChildren = when (event) {
            is MousePressEvent -> onMousePress(event)
            is MouseReleaseEvent -> onMouseRelease(event)
            is MouseScrollEvent -> onMouseScroll(event)
            is MouseDragEvent -> onMouseDrag(event)
            is KeyPressEvent -> onKeyPress(event)
            is KeyReleaseEvent -> onKeyRelease(event)
            is CharTypedEvent -> onCharTyped(event)
        }

        if (updateChildren) children.forEach { it.dispatchInputEvent(event) }
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