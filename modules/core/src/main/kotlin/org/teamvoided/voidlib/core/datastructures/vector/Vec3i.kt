package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.*

@Serializable
data class Vec3i(var x: Int, var y: Int, var z: Int) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec3i {
            return Vec3i(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"))
        }
    }

    operator fun unaryPlus() = Vec3i(+x, +y, +z)
    operator fun unaryMinus() = Vec3i(-x, -y, +z)

    operator fun inc(): Vec3i {
        this.x++
        this.y++
        this.z++
        return this
    }

    operator fun dec(): Vec3i {
        this.x--
        this.y--
        this.z--
        return this
    }

    operator fun plus(toAdd: Vec3i): Vec3i = Vec3i(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z))
    operator fun plus(toAdd: Number): Vec3i = Vec3i(x = (this.x + toAdd).i, y = (this.y + toAdd).i, z = (this.z + toAdd).i)

    operator fun minus(toSubtract: Vec3i): Vec3i = Vec3i(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z))
    operator fun minus(toSubtract: Number): Vec3i = Vec3i(x = (this.x - toSubtract).i, y = (this.y - toSubtract).i, z = (this.z - toSubtract).i)

    operator fun times(toMultiply: Vec3i): Vec3i = Vec3i(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z))
    operator fun times(toMultiply: Number): Vec3i = Vec3i(x = (this.x * toMultiply.d).i, y = (this.y * toMultiply.d).i, z = (this.z * toMultiply.d).i)

    operator fun div(toDivide: Vec3i): Vec3i = Vec3i(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z))
    operator fun div(toDivide: Number): Vec3i = Vec3i(x = (this.x / toDivide).i, y = (this.y / toDivide).i, z = (this.z / toDivide).i)

    operator fun rem(toRem: Vec3i): Vec3i = Vec3i(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z))
    operator fun rem(toRem: Number): Vec3i = Vec3i(x = (this.x % toRem).i, y = (this.y % toRem).i, z = (this.z % toRem).i)

    operator fun contains(int: Int): Boolean = this.x == int || this.y == int || this.z == int

    operator fun compareTo(toCompare: Vec3i): Int {
        val totalValThis = this.x + this.y + this.z
        val totalValCompare = toCompare.x + toCompare.y + toCompare.z

        return (totalValThis - totalValCompare)
    }

    fun add(x: Int, y: Int, z: Int): Vec3i = Vec3i(x = this.x + x, y = this.y + y, this.z + z)
    fun addAssign(x: Int, y: Int, z: Int) {
        this.x += x
        this.y += y
        this.z += z
    }

    fun normalize(): Vec3i {
        val invLength = Math.invsqrt(Math.fma(x.toDouble(), x.toDouble(), Math.fma(y.toDouble(), y.toDouble(), z.toDouble() * z.toDouble())))
        x = (x * invLength).toInt()
        y = (y * invLength).toInt()
        z = (z * invLength).toInt()
        return this
    }

    fun normalize(dest: Vec3i): Vec3i {
        val invLength = Math.invsqrt(Math.fma(x.toDouble(), x.toDouble(), Math.fma(y.toDouble(), y.toDouble(), z.toDouble() * z.toDouble())))
        dest.x = (x * invLength).toInt()
        dest.y = (y * invLength).toInt()
        dest.z = (z * invLength).toInt()
        return dest
    }

    fun copy(): Vec3i {
        return Vec3i(this.x, this.y, this.z)
    }

    fun to3f() = Vec3f(x.f, y.f, z.f)
    fun to3d() = Vec3d(x.d, y.d, z.d)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putInt("x", x)
        compound.putInt("y", y)
        compound.putInt("z", z)

        return compound
    }
}
