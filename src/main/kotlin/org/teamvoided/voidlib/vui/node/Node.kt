package org.teamvoided.voidlib.vui.node

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.InputEvent
import org.teamvoided.voidlib.vui.Named
import org.teamvoided.voidlib.vui.UpdateContext
import org.teamvoided.voidlib.vui.registry.VuiRegistries
import java.util.LinkedList

abstract class Node protected constructor(): Named {
    private var parent: Node? = null
    private val children = LinkedList<Node>()

    var globalPos: Vec2i
        get() = if (parent != null) (parent!!.globalPos + pos) else pos
        set(value) = if (parent != null) pos = (value - parent!!.pos)  else pos = value
    var pos = Vec2i(0, 0)
    var size = Vec2i(0, 0)

    protected open var onMousePress: ((InputEvent.MousePressEvent) -> Unit)? = null
    protected open var onMouseRelease: ((InputEvent.MouseReleaseEvent) -> Unit)? = null
    protected open var onMouseScroll: ((InputEvent.MouseScrollEvent) -> Unit)? = null
    protected open var onMouseDrag: ((InputEvent.MouseDragEvent) -> Unit)? = null
    protected open var onKeyPress: ((InputEvent.KeyPressEvent) -> Unit)? = null
    protected open var onKeyRelease: ((InputEvent.KeyReleaseEvent) -> Unit)? = null
    protected open var onCharTyped: ((InputEvent.CharTypedEvent) -> Unit)? = null
    protected open var updateCallback: ((UpdateContext) -> Unit)? = null
    protected open var drawCallback: ((DrawContext) -> Unit)? = null

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

    open fun isTouching(point: Vec2i): Boolean {
        return point.x >= this.globalPos.x && point.x <= this.globalPos.x + size.x &&
                point.y >= this.globalPos.y && point.y <= this.globalPos.y + size.y
    }

    open fun dispatchInputEvent(event: InputEvent) {
        when (event) {
            is InputEvent.MousePressEvent -> onMousePress?.invoke(event)
            is InputEvent.MouseReleaseEvent -> onMouseRelease?.invoke(event)
            is InputEvent.MouseScrollEvent -> onMouseScroll?.invoke(event)
            is InputEvent.MouseDragEvent -> onMouseDrag?.invoke(event)
            is InputEvent.KeyPressEvent -> onKeyPress?.invoke(event)
            is InputEvent.KeyReleaseEvent -> onKeyRelease?.invoke(event)
            is InputEvent.CharTypedEvent -> onCharTyped?.invoke(event)
        }

        children.forEach { it.dispatchInputEvent(event) }
    }

    open fun update(context: UpdateContext) {
        updateCallback?.invoke(context)

        children.forEach { it.update(context) }
    }

    open fun draw(context: DrawContext) {
        drawCallback?.invoke(context)

        children.forEach { it.draw(context) }
    }

    abstract fun typeId(): Identifier

    fun type(): Type<out Node> =
        VuiRegistries.NODE_TYPE.get(typeId())!!

    fun children(): List<Node> = children

    fun parent() = parent

    data class Type<T: Node>(val serialize: (T, NbtCompound) -> Unit, val deserialize: (NbtCompound) -> T)
}