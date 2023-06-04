package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.ARGB
import kotlin.math.ceil

@Serializable
data class Vec4d(var x: Double, var y: Double, var z: Double, var w: Double) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec4d {
            return Vec4d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"), compound.getDouble("w"))
        }
    }

    operator fun unaryPlus() = Vec4d(+x, +y, +z, +w)
    operator fun unaryMinus() = Vec4d(-x, -y, +z, +w)

    operator fun inc(): Vec4d {
        this.x++
        this.y++
        this.z++
        this.w++
        return this
    }

    operator fun dec(): Vec4d {
        this.x--
        this.y--
        this.z--
        this.w--
        return this
    }

    operator fun plus(toAdd: Vec4d): Vec4d = Vec4d(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z), w = (this.w + toAdd.w))
    operator fun plus(toAdd: Double): Vec4d = Vec4d(x = (this.x + toAdd), y = (this.y + toAdd), z = (this.z + toAdd), w = (this.w + toAdd))

    operator fun minus(toSubtract: Vec4d): Vec4d = Vec4d(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z), w = (this.w - toSubtract.w))
    operator fun minus(toSubtract: Double): Vec4d = Vec4d(x = (this.x - toSubtract), y = (this.y - toSubtract), z = (this.z - toSubtract), w = (this.w - toSubtract))

    operator fun times(toMultiply: Vec4d): Vec4d = Vec4d(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z), w = (this.w * toMultiply.w))
    operator fun times(toMultiply: Double): Vec4d = Vec4d(x = (this.x * toMultiply), y = (this.y * toMultiply), z = (this.z * toMultiply), w = (this.w * toMultiply))

    operator fun div(toDivide: Vec4d): Vec4d = Vec4d(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z), w = (this.w / toDivide.w))
    operator fun div(toDivide: Double): Vec4d = Vec4d(x = (this.x / toDivide), y = (this.y / toDivide), z = (this.z / toDivide), w = (this.w / toDivide))

    operator fun rem(toRem: Vec4d): Vec4d = Vec4d(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z), w = (this.w % toRem.w))
    operator fun rem(toRem: Double): Vec4d = Vec4d(x = (this.x % toRem), y = (this.y % toRem), z = (this.z % toRem), w = (this.w % toRem))

    operator fun contains(int: Double): Boolean = this.x == int || this.y == int || this.z == int || this.w == int

    operator fun compareTo(toCompare: Vec4d): Int {
        val totalValThis = this.x + this.y + this.z + this.w
        val totalValCompare = toCompare.x + toCompare.y + toCompare.z + this.w

        return ceil(totalValThis - totalValCompare).toInt()
    }

    fun add(x: Int, y: Int, z: Int, w: Int): Vec4d = Vec4d(x = this.x + x, y = this.y + y, this.z + z, this.w + w)
    fun addAssign(x: Int, y: Int, z: Int, w: Int) {
        this.x += x
        this.y += y
        this.z += z
        this.w += w
    }

    fun normalize(): Vec4d {
        val invLength: Double = 1.0 / length()
        x = x * invLength
        y = y * invLength
        z = z * invLength
        w = w * invLength
        return this
    }

    fun normalize(dest: Vec4d): Vec4d {
        val invLength: Double = 1.0 / length()
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        dest.w = w * invLength
        return dest
    }

    fun normalize(length: Double): Vec4d {
        val invLength: Double = 1.0 / length() * length
        x *= invLength
        y *= invLength
        z *= invLength
        w *= invLength
        return this
    }

    fun normalize(length: Double, dest: Vec4d): Vec4d {
        val invLength: Double = 1.0 / length() * length
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        dest.w = w * invLength
        return dest
    }

    fun length(): Double {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))))
    }

    fun copy(): Vec4d {
        return Vec4d(this.x, this.y, this.z, this.w)
    }

    fun toARGB() = ARGB((x * 255).toUInt().toUByte(), (y * 255).toUInt().toUByte(), (z * 255).toUInt().toUByte(), (w * 255).toUInt().toUByte())

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putDouble("x", x)
        compound.putDouble("y", y)
        compound.putDouble("z", z)
        compound.putDouble("w", w)

        return compound
    }
}
