package org.team.voided.voidlib

import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.fwfc.FWFC
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.LinkedList
import java.util.List.copyOf

const val MODID = "voidlib"

private val modules: MutableList<LibModule> = LinkedList()
@JvmField val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun onInitialize() {
    addModule(FWFC())

    modules.forEach(LibModule::commonSetup)
}

fun addModule(module: LibModule) {
    modules.add(module)
}

fun getModules(): List<LibModule> = copyOf(modules)

fun id(path: String): Identifier {
    return Identifier(MODID, path)
}