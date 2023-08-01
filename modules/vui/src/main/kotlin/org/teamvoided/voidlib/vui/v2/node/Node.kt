package org.teamvoided.voidlib.vui.v2.node

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.id
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent.*
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEvent.*
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.DrawContext
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext.UpdateContext
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.CallbackManager
import org.teamvoided.voidlib.vui.v2.node.layout.BoundingBox
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorStyle
import java.util.*
import kotlin.math.min

abstract class Node(): CallbackManager() {
    private var parent: Node? = null
    private val children: MutableList<Node> = LinkedList<Node>()

    var globalPos: Vec2i
        get() = if (parent != null) (parent!!.globalPos + pos) else pos
        set(value) = if (parent != null) pos = (value - parent!!.pos)  else pos = value
    var pos = Vec2i(0, 0)
    var size = Vec2i(0, 0)

    override val callbacks = HashMap<Identifier, Callback<*>>()

    val mousePressCallback: Callback<MousePressEvent>
        get() = getCallbackAs(id("mouse_press_callback"))

    val mouseReleaseCallback: Callback<MouseReleaseEvent>
        get() = getCallbackAs(id("mouse_release_callback"))

    val mouseScrollCallback: Callback<MouseScrollEvent>
        get() = getCallbackAs(id("mouse_sroll_callback"))

    val mouseDragCallback: Callback<MouseDragEvent>
        get() = getCallbackAs(id("mouse_drag_callback"))

    val keyPressCallback: Callback<KeyPressEvent>
        get() = getCallbackAs(id("key_press_callback"))

    val keyReleaseCallback: Callback<KeyReleaseEvent>
        get() = getCallbackAs(id("key_release_callback"))

    val charTypedCallback: Callback<CharTypedEvent>
        get() = getCallbackAs(id("char_typed_callback"))

    val updateCallback: Callback<UpdateEvent>
        get() = getCallbackAs(id("update_callback"))

    val drawCallback: Callback<DrawEvent>
        get() = getCallbackAs(id("draw_callback"))

    val addChildCallback: Callback<List<Node>>
        get() = getCallbackAs(id("child_add_callback"))

    val removeChildCallback: Callback<List<Node>>
        get() = getCallbackAs(id("child_remove_callback"))

    protected constructor(pos: Vec2i): this() {
        this.pos = pos
    }

    protected constructor(pos: Vec2i, size: Vec2i): this() {
        this.pos = pos
        this.size = size
    }

    protected open fun onMousePress(event: MousePressEvent) { }

    protected open fun onMouseRelease(event: MouseReleaseEvent) { }

    protected open fun onMouseScroll(event: MouseScrollEvent) { }

    protected open fun onMouseDrag(event: MouseDragEvent) { }

    protected open fun onKeyPress(event: KeyPressEvent) { }

    protected open fun onKeyRelease(event: KeyReleaseEvent) { }

    protected open fun onCharTyped(event: CharTypedEvent) { }

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

        addChildCallback(children.toList())
    }

    fun removeChild(child: Node) {
        children.remove(child)

        val oldGlobal = child.globalPos

        child.parent = null
        child.globalPos = oldGlobal

        removeChildCallback(children.toList())
    }

    fun boundTo(boundingBox: BoundingBox, keepProportions: Boolean = true) {
        if (globalPos.x < boundingBox.pos.x) globalPos = Vec2i(boundingBox.pos.x, globalPos.y)
        if (globalPos.x + size.x > boundingBox.pos.x + boundingBox.size.x) globalPos = Vec2i(boundingBox.pos.x + boundingBox.size.x - size.x, globalPos.y)

        if (globalPos.y < boundingBox.pos.y) globalPos = Vec2i(globalPos.x, boundingBox.pos.y)
        if (globalPos.y + size.y > boundingBox.pos.y + boundingBox.size.y) globalPos = Vec2i(globalPos.x, boundingBox.pos.y + boundingBox.size.y - size.y)

        if (keepProportions) {
            val scale = min(boundingBox.size.x, boundingBox.size.y / size.y)
            if (scale < 1) size *= scale
        } else {
            if (size.x > boundingBox.size.x) size.x = boundingBox.size.x
            if (size.y > boundingBox.size.y) size.y = boundingBox.size.y
        }

        children.forEach { it.boundTo(boundingBox, keepProportions) }
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
        when (event) {
            is MousePressEvent -> {onMousePress(event); mousePressCallback(event)}
            is MouseReleaseEvent -> {onMouseRelease(event); mouseReleaseCallback(event) }
            is MouseScrollEvent -> {onMouseScroll(event); mouseScrollCallback(event) }
            is MouseDragEvent -> {onMouseDrag(event); mouseDragCallback(event) }
            is KeyPressEvent -> {onKeyPress(event); keyPressCallback(event) }
            is KeyReleaseEvent -> {onKeyRelease(event); keyReleaseCallback(event) }
            is CharTypedEvent -> {onCharTyped(event); charTypedCallback(event) }
        }

        return if (!event.canceled()) {
            children.forEach {
                val b = it.dispatchInputEvent(event)
                if (!b) return false
            }; return true
        } else false
    }

    fun dispatchLogicalEvent(context: LogicalEventContext) {
        val event: Event.LogicalEvent
        when (context) {
            is DrawContext -> {
                event = DrawEvent(context, State.PreChild)
                draw(event)
                drawCallback(event)
            }
            is UpdateContext -> {
                event = UpdateEvent(context, State.PreChild)
                update(event)
                updateCallback(event)
            }
        }

        if (!event.canceled()) children.forEach { it.dispatchLogicalEvent(context) }

        when (context) {
            is DrawContext -> draw(DrawEvent(context, State.PostChild))
            is UpdateContext -> update(UpdateEvent(context, State.PostChild))
        }
    }

    fun asBoundingBox() = BoundingBox.of(globalPos, size)

    fun children(): List<Node> = children

    fun parent() = parent

    data class Type<T: Node>(val deserialize: (NbtCompound) -> T)
}