package org.teamvoided.voidlib.vui.v2.screen

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.mixin.ScreenInvoker
import org.teamvoided.voidlib.vui.v2.event.Callback
import org.teamvoided.voidlib.vui.v2.event.CallbackManager
import org.teamvoided.voidlib.vui.v2.event.ui.Event.InputEvent
import org.teamvoided.voidlib.vui.v2.event.ui.Event.LogicalEventContext
import org.teamvoided.voidlib.vui.v2.node.Node
import org.teamvoided.voidlib.vui.v2.screen.cursor.CursorAdapter


open class VoidUIAdapter<R : Node> protected constructor(protected var pos: Vec2i, protected var size: Vec2i, val rootNode: R): CallbackManager(), Element, Drawable, Selectable {
    val cursorAdapter: CursorAdapter = CursorAdapter.ofClientWindow()
    protected var disposed = false

    override val callbacks = HashMap<Identifier, Callback<*>>()
    val updateCallback: Callback<LogicalEventContext.UpdateContext>
        get() = getCallbackAs(id("update_callback"))
    val drawCallback: Callback<LogicalEventContext.DrawContext>
        get() = getCallbackAs(id("draw_callback"))

    fun inflateAndMount() {
        rootNode.inflate(size)
        rootNode.dismount()
        rootNode.pos = pos
    }

    fun moveAndResize(pos: Vec2i, size: Vec2i) {
        this.pos = pos
        this.size = size
        inflateAndMount()
    }

    fun dispose() {
        cursorAdapter.dispose()
        disposed = true
    }

    fun pos() = pos
    fun size() = size

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        try {
            isRendering = true
            val delta = MinecraftClient.getInstance().lastFrameDuration
            val window = MinecraftClient.getInstance().window
            val updateContext = LogicalEventContext.UpdateContext(delta, Vec2i(mouseX, mouseY))
            rootNode.dispatchLogicalEvent(updateContext)
            updateCallback(updateContext)
            RenderSystem.enableDepthTest()
            GlStateManager._enableScissorTest()
            GlStateManager._scissorBox(0, 0, window.framebufferWidth, window.framebufferHeight)
            val drawContext = LogicalEventContext.DrawContext(matrices, Vec2i(mouseX, mouseY), partialTicks, delta)
            rootNode.dispatchLogicalEvent(drawContext)
            drawCallback(drawContext)
            GlStateManager._disableScissorTest()
            RenderSystem.disableDepthTest()
            val hovered = rootNode.childAt(mouseX, mouseY)
            if (!disposed && hovered != null) {
                cursorAdapter.applyStyle(hovered.cursorStyle())
            }
        } finally {
            isRendering = false
        }
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return rootNode.isTouching(Vec2d(mouseX, mouseY))
    }

    override fun setFocused(focused: Boolean) {}
    override fun isFocused(): Boolean {
        return true
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.MousePressEvent(Vec2d(mouseX, mouseY), button))
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.MouseReleaseEvent(Vec2d(mouseX, mouseY), button))
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.MouseScrollEvent(Vec2d(mouseX, mouseY), amount))
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.MouseDragEvent(Vec2d(mouseX, mouseY), Vec2d(deltaX, deltaY), button))
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.KeyPressEvent(keyCode, scanCode, modifiers))
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.KeyReleaseEvent(keyCode, scanCode, modifiers))
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return rootNode.dispatchInputEvent(InputEvent.CharTypedEvent(chr, modifiers))
    }

    override fun getType(): Selectable.SelectionType {
        return Selectable.SelectionType.NONE
    }

    override fun appendNarrations(builder: NarrationMessageBuilder) {}

    companion object {
        var isRendering = false
            private set

        fun <R : Node> create(
            screen: Screen,
            rootComponentMaker: (Vec2i, Vec2i) -> R
        ): VoidUIAdapter<R> {
            val rootComponent = rootComponentMaker(Vec2i(0, 0), Vec2i(screen.width, screen.height))
            val adapter = VoidUIAdapter(Vec2i(0, 0), Vec2i(screen.width, screen.height), rootComponent)
            (screen as ScreenInvoker).void_addDrawableChild(adapter)
            screen.focusOn(adapter)
            return adapter
        }

        fun <R : Node> createWithoutScreen(
            pos: Vec2i,
            size: Vec2i,
            rootComponentMaker: (Vec2i, Vec2i) -> R
        ): VoidUIAdapter<R> {
            val rootComponent = rootComponentMaker(pos, size)
            return VoidUIAdapter(pos, size, rootComponent)
        }
    }
}