package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.impl.VuiEditor
import org.teamvoided.voidlib.vui.node.*
import org.teamvoided.voidlib.vui.node.screen.VuiScreen

class EditorScreen(title: Text): VuiScreen(title) {
    override val parent: Node = ParentNode(title.toString())

    private val background = SpriteNode(
        Vec2i(0, 0),
        Vec2i(1920, 1080), {
        VuiSpriteManager.getSprite(id("vres/editor_bg"))!!
    }, "bg")

    private val nodeTreeScrollable = ScrollableNode(Vec2i(0, 0), Vec2i(400, MinecraftClient.getInstance().window.height - 200), ScrollableNode.ScrollAxis.AXIS_Y, "scrollable")
    private val nodeTreeBg = SpriteNode(Vec2i(0, 0), Vec2i(400, MinecraftClient.getInstance().window.height - 200), { VuiSpriteManager.getSprite(id("vres/editor_tree"))!! }, "nodeTreeBg")
    private val nodeTree = ExpandableNode(Vec2i(20, 20), Vec2i(380, MinecraftClient.getInstance().window.height - 220), "nodeTree")

    override var initCallback: (() -> Unit)? = {
        nodeTreeScrollable.addChild(nodeTreeBg)
        nodeTreeScrollable.addChild(nodeTree)

        parent.addChild(background)
        parent.addChild(
            MovableNode(SpriteNode(
                Vec2i(0, 0),
                Vec2i(32, 32), {
                VuiSpriteManager.getSprite(id("vres/icon"))!!
            }, "icon"), "movable")
        )
        parent.addChild(nodeTreeScrollable)

        if (VuiEditor.stopMusic) {
            MinecraftClient.getInstance().soundManager.stopAll()
            MinecraftClient.getInstance().musicTracker.stop()
        }
    }

    override fun resize(client: MinecraftClient?, width: Int, height: Int) {
        background.size = Vec2i(width, height)
        nodeTreeScrollable.size.y = MinecraftClient.getInstance().window.height - 200
        nodeTreeBg.size.y = MinecraftClient.getInstance().window.height - 200
        nodeTree.size.y = MinecraftClient.getInstance().window.height - 220
    }

    override fun shouldCloseOnEsc(): Boolean {
        return false
    }
}