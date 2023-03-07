package org.teamvoided.voidlib.vui.registry

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import org.teamvoided.voidlib.id
import org.teamvoided.voidlib.vui.node.Node

object VuiRegistries {
    val NODE_TYPE: Registry<Node.Type<*>> = FabricRegistryBuilder.createSimple(Node.Type::class.java, id("vui_node_type_registry")).buildAndRegister()
}