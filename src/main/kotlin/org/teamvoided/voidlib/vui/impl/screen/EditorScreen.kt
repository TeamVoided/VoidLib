package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.node.*
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen

class EditorScreen : VuiScreen(Text.literal("Vui Editor")) {
    override val parent = ParentNode()

    val bg = ColorNode(Vec2i(0,0), Vec2i(1000, 1000), ARGB(0, 0, 0, 255))

    val colorNode = ColorNode(Vec2i(0,0), Vec2i(100, 100), ARGB(255, 148, 49, 49))
    val input = NumberInputNode(Vec2i(0, 0), Vec2i(100, 100))
    val mov1 = MovableNode(colorNode)

    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here

        parent.addChild(bg)
        mov1.addChild(input)
        bg.addChild(mov1)

    }
}