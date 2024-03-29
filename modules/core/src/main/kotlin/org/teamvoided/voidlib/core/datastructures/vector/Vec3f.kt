package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.*
import kotlin.math.ceil

@Serializable
data class Vec3f(var x: Float, var y: Float, var z: Float) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec3f {
            return Vec3f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"))
        }
    }

    operator fun unaryPlus() = Vec3f(+x, +y, +z)
    operator fun unaryMinus() = Vec3f(-x, -y, +z)

    operator fun inc(): Vec3f {
        this.x++
        this.y++
        this.z++
        return this
    }

    operator fun dec(): Vec3f {
        this.x--
        this.y--
        this.z--
        return this
    }

    operator fun plus(toAdd: Vec3f): Vec3f = Vec3f(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z))
    operator fun plus(toAdd: Number): Vec3f = Vec3f(x = (this.x + toAdd).f, y = (this.y + toAdd).f, z = (this.z + toAdd).f)

    operator fun minus(toSubtract: Vec3f): Vec3f = Vec3f(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z))
    operator fun minus(toSubtract: Number): Vec3f = Vec3f(x = (this.x - toSubtract).f, y = (this.y - toSubtract).f, z = (this.z - toSubtract).f)

    operator fun times(toMultiply: Vec3f): Vec3f = Vec3f(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z))
    operator fun times(toMultiply: Number): Vec3f = Vec3f(x = (this.x * toMultiply.d).f, y = (this.y * toMultiply.d).f, z = (this.z * toMultiply.d).f)

    operator fun div(toDivide: Vec3f): Vec3f = Vec3f(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z))
    operator fun div(toDivide: Number): Vec3f = Vec3f(x = (this.x / toDivide).f, y = (this.y / toDivide).f, z = (this.z / toDivide).f)

    operator fun rem(toRem: Vec3f): Vec3f = Vec3f(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z))
    operator fun rem(toRem: Number): Vec3f = Vec3f(x = (this.x % toRem).f, y = (this.y % toRem).f, z = (this.z % toRem).f)

    operator fun contains(int: Float): Boolean = this.x == int || this.y == int || this.z == int

    operator fun compareTo(toCompare: Vec3f): Int {
        val totalValThis = this.x + this.y + this.z
        val totalValCompare = toCompare.x + toCompare.y + toCompare.z

        return ceil(totalValThis - totalValCompare).i
    }

    fun add(x: Int, y: Int, z: Int): Vec3f = Vec3f(x = this.x + x, y = this.y + y, this.z + z)
    fun addAssign(x: Int, y: Int, z: Int) {
        this.x += x
        this.y += y
        this.z += z
    }

    fun normalize(): Vec3f {
        val invLength = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)))
        x *= invLength
        y *= invLength
        z *= invLength
        return this
    }

    fun normalize(dest: Vec3f): Vec3f {
        val invLength = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)))
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        return dest
    }

    fun copy(): Vec3f {
        return Vec3f(this.x, this.y, this.z)
    }

    fun to3i() = Vec3i(x.i, y.i, z.i)
    fun to3d() = Vec3d(x.d, y.d, z.d)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putFloat("x", x)
        compound.putFloat("y", y)
        compound.putFloat("z", z)

        return compound
    }
}
