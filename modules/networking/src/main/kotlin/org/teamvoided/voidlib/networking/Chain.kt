package org.teamvoided.voidlib.networking

import net.minecraft.util.Identifier

interface Chain {
    val chainId: Identifier

    class EmptyChain(override val chainId: Identifier): Chain
}