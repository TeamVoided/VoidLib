package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import kotlin.math.ceil

@Serializable
data class Vec3d(var x: Double, var y: Double, var z: Double) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec3d {
            return Vec3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z"))
        }
    }

    operator fun unaryPlus() = Vec3d(+x, +y, +z)
    operator fun unaryMinus() = Vec3d(-x, -y, +z)

    operator fun inc(): Vec3d {
        this.x++
        this.y++
        this.z++
        return this
    }

    operator fun dec(): Vec3d {
        this.x--
        this.y--
        this.z--
        return this
    }

    operator fun plus(toAdd: Vec3d): Vec3d = Vec3d(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z))
    operator fun plus(toAdd: Double): Vec3d = Vec3d(x = (this.x + toAdd), y = (this.y + toAdd), z = (this.z + toAdd))

    operator fun minus(toSubtract: Vec3d): Vec3d = Vec3d(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z))
    operator fun minus(toSubtract: Double): Vec3d = Vec3d(x = (this.x - toSubtract), y = (this.y - toSubtract), z = (this.z - toSubtract))

    operator fun times(toMultiply: Vec3d): Vec3d = Vec3d(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z))
    operator fun times(toMultiply: Double): Vec3d = Vec3d(x = (this.x * toMultiply), y = (this.y * toMultiply), z = (this.z * toMultiply))

    operator fun div(toDivide: Vec3d): Vec3d = Vec3d(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z))
    operator fun div(toDivide: Double): Vec3d = Vec3d(x = (this.x / toDivide), y = (this.y / toDivide), z = (this.z / toDivide))

    operator fun rem(toRem: Vec3d): Vec3d = Vec3d(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z))
    operator fun rem(toRem: Double): Vec3d = Vec3d(x = (this.x % toRem), y = (this.y % toRem), z = (this.z % toRem))

    operator fun contains(int: Double): Boolean = this.x == int || this.y == int || this.z == int

    operator fun compareTo(toCompare: Vec3d): Int {
        val totalValThis = this.x + this.y + this.z
        val totalValCompare = toCompare.x + toCompare.y + toCompare.z

        return ceil(totalValThis - totalValCompare).toInt()
    }

    fun add(x: Int, y: Int, z: Int): Vec3d = Vec3d(x = this.x + x, y = this.y + y, this.z + z)
    fun addAssign(x: Int, y: Int, z: Int) {
        this.x += x
        this.y += y
        this.z += z
    }

    fun normalize(): Vec3d {
        val invLength = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)))
        x *= invLength
        y *= invLength
        z *= invLength
        return this
    }

    fun normalize(dest: Vec3d): Vec3d {
        val invLength = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)))
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        return dest
    }

    fun copy(): Vec3d {
        return Vec3d(this.x, this.y, this.z)
    }

    fun to3i() = Vec3i(x.toInt(), y.toInt(), z.toInt())
    fun to3f() = Vec3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putDouble("x", x)
        compound.putDouble("y", y)
        compound.putDouble("z", z)

        return compound
    }
}
