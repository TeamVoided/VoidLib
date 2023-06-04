package org.teamvoided.voidlib.vui.v2.screen

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.event.fabric.WindowResizeCallback
import org.teamvoided.voidlib.vui.v2.node.Node

abstract class VuiScreen<R: Node>(title: Text): Screen(title) {
    abstract val uiAdapter: VoidUIAdapter<R>
    val root get() = uiAdapter.rootNode
    protected open var scale = 1.0
    protected var oldScaleFactor = 1.0

    open fun vuiInit() {}

    override fun init() {
        val window = MinecraftClient.getInstance().window
        oldScaleFactor = window.scaleFactor
        window.scaleFactor = 1.0
        this.width = window.scaledWidth
        this.height = window.scaledHeight

        addDrawableChild(uiAdapter)
        uiAdapter.inflateAndMount()
        focusOn(uiAdapter)

        WindowResizeCallback.event.register { _, w ->
            this.width = w.scaledWidth
            this.height = w.scaledHeight

            uiAdapter.moveAndResize(Vec2i(0, 0), Vec2i(width, height))
        }

        vuiInit()
        super.init()
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return uiAdapter.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun close() {
        MinecraftClient.getInstance().window.scaleFactor = oldScaleFactor
        uiAdapter.dispose()
        super.close()
    }

    override fun removed() {
        uiAdapter.dispose()
    }
}