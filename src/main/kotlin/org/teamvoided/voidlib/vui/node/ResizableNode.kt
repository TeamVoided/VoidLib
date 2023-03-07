package org.teamvoided.voidlib.vui.node

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.vui.node.NodeIds.RESIZABLE

open class ResizableNode(override var name: String) : Node() {
    override fun typeId(): Identifier = RESIZABLE
}