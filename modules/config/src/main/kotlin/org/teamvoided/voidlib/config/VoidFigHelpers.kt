package org.teamvoided.voidlib.config

import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import java.io.File

object VoidFigHelpers {
    fun getConfigFile(id: Identifier, side: Side = Side.COMMON, format: String): File
        = when (side) {
            Side.COMMON -> {
                File("config/${id.namespace}/${id.path}.$format")
            } Side.SERVER -> {
                File("config/server/${id.namespace}/${id.path}.$format")
            } Side.CLIENT -> {
                File("config/client/${id.namespace}/${id.path}.$format")
            }
        }

    fun getConfigFile(voidFig: VoidFig<*>) =
        getConfigFile(voidFig.id, voidFig.side, voidFig.fileType)

    fun getConfigData(voidFig: VoidFig<*>): String {
        voidFig.serialize()
        return getConfigFile(voidFig.id, voidFig.side, voidFig.fileType).readText()
    }

    @ApiStatus.Internal
    fun writeConfigData(voidFig: VoidFig<*>, rawData: String) {
        val file = getConfigFile(voidFig.id, voidFig.side, voidFig.fileType)
        file.writeText(rawData)
    }
}
