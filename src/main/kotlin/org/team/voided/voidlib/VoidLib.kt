package org.team.voided.voidlib

import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.cresm.CResM
import org.team.voided.voidlib.wfc.WFC
import java.util.*
import java.util.List.copyOf

const val MODID = "voidlib"

private val modules: MutableList<LibModule> = LinkedList()
@JvmField val LOGGER: Logger = LoggerFactory.getLogger(MODID)
@JvmField val TESTOBJ_ITEM = Item(Item.Settings())

@Suppress("unused")
fun onInitialize() {
    addModule(WFC())
    addModule(CResM())
    modules.forEach(LibModule::commonSetup)

    Registry.register(Registry.ITEM, id("testobj"), TESTOBJ_ITEM)
}

fun addModule(module: LibModule) {
    modules.add(module)
}

fun getModules(): List<LibModule> = copyOf(modules)

fun id(path: String): Identifier {
    return Identifier(MODID, path)
}