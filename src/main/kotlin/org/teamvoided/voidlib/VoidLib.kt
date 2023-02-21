package org.teamvoided.voidlib

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.teamvoided.voidlib.core.LibModule
import org.teamvoided.voidlib.cresm.CResM
import org.teamvoided.voidlib.dimutil.DimUtil
import org.teamvoided.voidlib.pow.Pow
import org.teamvoided.voidlib.wfc.WFC
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
    addModule(DimUtil())
    modules.forEach(LibModule::commonSetup)
}

fun addModule(module: LibModule) {
    modules.add(module)
}

fun getModules(): List<LibModule> = copyOf(modules)

fun id(path: String): Identifier {
    return Identifier(MODID, path)
}