package org.teamvoided.voidlib.vui.node

import net.minecraft.util.Identifier
import org.teamvoided.voidlib.vui.node.NodeIds.PARENT

class ParentNode(override var name: String) : Node() {
    override fun typeId(): Identifier = PARENT
}