package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.text.Text
import org.teamvoided.voidlib.vui.v2.node.ParentNode
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen

class EditorScreen: VuiScreen(Text.literal("Vui Editor")) {
    override val parent = ParentNode()

    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here
    }
}