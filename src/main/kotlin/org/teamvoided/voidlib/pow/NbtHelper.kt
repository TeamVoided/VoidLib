package org.teamvoided.voidlib.pow

import net.minecraft.nbt.NbtCompound
import java.math.BigDecimal

class NbtHelper {
    companion object {
        fun writeToCompound(nbt: NbtCompound, key: String, amount: BigDecimal) {
            nbt.putString(key, amount.toString())
        }

        fun readFromCompound(nbt: NbtCompound, key: String): BigDecimal {
            return BigDecimal(nbt.getString(key))
        }
    }
}