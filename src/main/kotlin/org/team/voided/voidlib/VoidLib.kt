package org.team.voided.voidlib

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.team.voided.voidlib.core.LibModule
import org.team.voided.voidlib.cresm.CResM
import org.team.voided.voidlib.pow.Pow
import org.team.voided.voidlib.wfc.WFC
import java.util.*
import java.util.List.copyOf

const val MODID = "voidlib"

private val modules: MutableList<LibModule> = LinkedList()
@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@Suppress("unused")
fun onInitialize() {
    addModule(WFC())
    addModule(CResM())
    addModule(Pow())
    modules.forEach(LibModule::commonSetup)
}

fun addModule(module: LibModule) {
    modules.add(module)
}

fun getModules(): List<LibModule> = copyOf(modules)

fun id(path: String): Identifier {
    return Identifier(MODID, path)
}