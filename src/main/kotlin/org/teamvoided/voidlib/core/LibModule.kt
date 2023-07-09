package org.teamvoided.voidlib.core

import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
abstract class LibModule(val id: Identifier) {
    @ApiStatus.Internal
    abstract fun commonSetup()
    @ApiStatus.Internal
    abstract fun clientSetup()
}