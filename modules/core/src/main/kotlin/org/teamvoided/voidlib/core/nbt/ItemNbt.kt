@file:Suppress("unused")

package org.teamvoided.voidlib.core.nbt

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import org.teamvoided.voidlib.core.i
import java.util.*

fun ItemStack.put(key: String, element: NbtElement): NbtElement? = this.orCreateNbt.put(key, element)
fun ItemStack.putByte(key: String, byte: Byte) = this.orCreateNbt.putByte(key, byte)
fun ItemStack.putShort(key: String, short: Short) = this.orCreateNbt.putShort(key, short)
fun ItemStack.putInt(key: String, int: Int) = this.orCreateNbt.putInt(key, int)
fun ItemStack.putLong(key: String, long: Long) = this.orCreateNbt.putLong(key, long)
fun ItemStack.putUuid(key: String, uuid: UUID) = this.orCreateNbt.putUuid(key, uuid)
fun ItemStack.getUuid(key: String): UUID = this.orCreateNbt.getUuid(key)
fun ItemStack.containsUuid(key: String): Boolean = this.orCreateNbt.containsUuid(key)
fun ItemStack.putFloat(key: String, float: Float) = this.orCreateNbt.putFloat(key, float)
fun ItemStack.putDouble(key: String, double: Double) = this.orCreateNbt.putDouble(key, double)
fun ItemStack.putString(key: String, string: String) = this.orCreateNbt.putString(key, string)
fun ItemStack.putByteArray(key: String, byteArray: ByteArray) = this.orCreateNbt.putByteArray(key, byteArray)
fun ItemStack.putByteArray(key: String, byteArray: List<Byte>) = this.orCreateNbt.putByteArray(key, byteArray)
fun ItemStack.putIntArray(key: String, intArray: IntArray) = this.orCreateNbt.putIntArray(key, intArray)
fun ItemStack.putIntArray(key: String, intArray: List<Int>) = this.orCreateNbt.putIntArray(key, intArray)
fun ItemStack.putLongArray(key: String, longArray: LongArray) = this.orCreateNbt.putLongArray(key, longArray)
fun ItemStack.putLongArray(key: String, longArray: List<Long>) = this.orCreateNbt.putLongArray(key, longArray)
fun ItemStack.putBoolean(key: String, boolean: Boolean) = this.orCreateNbt.putBoolean(key, boolean)
fun ItemStack.get(key: String): NbtElement? = this.orCreateNbt[key]
fun ItemStack.getType(key: String): Byte = this.orCreateNbt.getType(key)
fun ItemStack.contains(key: String): Boolean = this.orCreateNbt.contains(key)
fun ItemStack.contains(key: String, type: Type): Boolean = this.orCreateNbt.contains(key, type.id)
fun ItemStack.getByte(key: String): Byte = this.orCreateNbt.getByte(key)
fun ItemStack.getShort(key: String): Short = this.orCreateNbt.getShort(key)
fun ItemStack.getInt(key: String): Int = this.orCreateNbt.getInt(key)
fun ItemStack.getLong(key: String): Long = this.orCreateNbt.getLong(key)
fun ItemStack.getFloat(key: String): Float = this.orCreateNbt.getFloat(key)
fun ItemStack.getDouble(key: String): Double = this.orCreateNbt.getDouble(key)
fun ItemStack.getString(key: String): String = this.orCreateNbt.getString(key)
fun ItemStack.getByteArray(key: String): ByteArray = this.orCreateNbt.getByteArray(key)
fun ItemStack.getIntArray(key: String): IntArray = this.orCreateNbt.getIntArray(key)
fun ItemStack.getLongArray(key: String): LongArray = this.orCreateNbt.getLongArray(key)
fun ItemStack.getCompound(key: String): NbtCompound = this.orCreateNbt.getCompound(key)
fun ItemStack.getList(key: String, type: Type): NbtList = this.orCreateNbt.getList(key, type.id)
fun ItemStack.getBoolean(key: String): Boolean = this.orCreateNbt.getBoolean(key)
fun ItemStack.remove(key: String) = this.orCreateNbt.remove(key)
fun ItemStack.isNbtEmpty(): Boolean = this.orCreateNbt.isEmpty
fun ItemStack.subNbt(key: String): NbtCompound = this.getOrCreateSubNbt(key)



enum class Type(val id: Int) {
    END_TYPE(NbtElement.END_TYPE),
    BYTE_TYPE(NbtElement.BYTE_TYPE),
    SHORT_TYPE(NbtElement.SHORT_TYPE),
    INT_TYPE(NbtElement.INT_TYPE),
    LONG_TYPE(NbtElement.LONG_TYPE),
    FLOAT_TYPE(NbtElement.FLOAT_TYPE),
    DOUBLE_TYPE(NbtElement.DOUBLE_TYPE),
    BYTE_ARRAY_TYPE(NbtElement.BYTE_ARRAY_TYPE),
    STRING_TYPE(NbtElement.STRING_TYPE),
    LIST_TYPE(NbtElement.LIST_TYPE),
    COMPOUND_TYPE(NbtElement.COMPOUND_TYPE),
    INT_ARRAY_TYPE(NbtElement.INT_ARRAY_TYPE),
    LONG_ARRAY_TYPE(NbtElement.LONG_ARRAY_TYPE),
    NUMBER_TYPE(NbtElement.NUMBER_TYPE);
    constructor(id:Byte): this(id.i)

}

