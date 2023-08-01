package org.teamvoided.voidlib.core

import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MODID = "voidlib"

@JvmField
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

fun id(path: String): Identifier = Identifier(MODID, path)