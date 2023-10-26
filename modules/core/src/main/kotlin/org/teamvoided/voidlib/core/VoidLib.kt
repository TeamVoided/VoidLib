package org.teamvoided.voidlib.core

import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApiStatus.Internal
const val MODID = "voidlib"

@JvmField
@ApiStatus.Internal
val LOGGER: Logger = LoggerFactory.getLogger(MODID)

@ApiStatus.Internal
fun id(path: String): Identifier = Identifier(MODID, path)