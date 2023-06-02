package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.block.Blocks
import net.minecraft.text.Text
import org.teamvoided.voidlib.core.RGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.core.datastructures.Vec3i
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.VuiSpriteManager
import org.teamvoided.voidlib.vui.v2.node.*
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen

class EditorScreen : VuiScreen(Text.literal("Vui Editor")) {
    override val parent = ParentNode()

    val bg = SpriteNode(Vec3i(0,0,0), Vec2i(1000,1000)) { VuiSpriteManager.getSprite(id("editor_bg"))!! }
    val block = BlockNode(Blocks.STONE.defaultState, null, Vec2i(0, 0), Vec2i(100, 100))
    val mov1 = MovableNode(block)

    val colorNode = ColorNode(Vec2i(100,100), Vec2i(200, 200), RGB(255,255,255))
//    val input = NumberInputNode(Vec2i(0, 0), Vec2i(100, 100))
    val mov2 = MovableNode(colorNode)

    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here

        parent.addChild(colorNode)
//        mov2.addChild(input)
//        bg.addChild(mov2)
//        bg.addChild(mov1)

    }
}