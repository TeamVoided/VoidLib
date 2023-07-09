package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.*
import kotlin.math.ceil

@Serializable
data class Vec2d(var x: Double, var y: Double) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec2d {
            return Vec2d(compound.getDouble("x"), compound.getDouble("y"))
        }
    }

    operator fun unaryPlus() = Vec2d(+x, +y)
    operator fun unaryMinus() = Vec2d(-x, -y)

    operator fun inc(): Vec2d {
        this.x++
        this.y++
        return this
    }

    operator fun dec(): Vec2d {
        this.x--
        this.y--
        return this
    }

    operator fun plus(toAdd: Vec2d): Vec2d = Vec2d(x = (this.x + toAdd.x), y = (this.y + toAdd.y))
    operator fun plus(toAdd: Number): Vec2d = Vec2d(x = (this.x + toAdd), y = (this.y + toAdd))

    operator fun minus(toSubtract: Vec2d): Vec2d = Vec2d(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y))
    operator fun minus(toSubtract: Number): Vec2d = Vec2d(x = (this.x - toSubtract), y = (this.y - toSubtract))

    operator fun times(toMultiply: Vec2d): Vec2d = Vec2d(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y))
    operator fun times(toMultiply: Number): Vec2d = Vec2d(x = (this.x * toMultiply), y = (this.y * toMultiply))

    operator fun div(toDivide: Vec2d): Vec2d = Vec2d(x = (this.x / toDivide.x), y = (this.y / toDivide.y))
    operator fun div(toDivide: Number): Vec2d = Vec2d(x = (this.x / toDivide), y = (this.y / toDivide))

    operator fun rem(toRem: Vec2d): Vec2d = Vec2d(x = (this.x % toRem.x), y = (this.y % toRem.y))
    operator fun rem(toRem: Number): Vec2d = Vec2d(x = (this.x % toRem), y = (this.y % toRem))

    operator fun contains(int: Double): Boolean = this.x == int || this.y == int

    operator fun compareTo(toCompare: Vec2d): Int {
        val totalValThis = this.x + this.y
        val totalValCompare = toCompare.x + toCompare.y

        return ceil(totalValThis - totalValCompare).i
    }

    fun add(x: Int, y: Int): Vec2d = Vec2d(x = this.x + x, y = this.y + y)
    fun addAssign(x: Int, y: Int) {
        this.x += x
        this.y += y
    }

    fun perpendicular(): Vec2d {
        val xTemp = y
        y = x * -1
        x = xTemp
        return this
    }

    fun normalize(): Vec2d {
        val invLength = Math.invsqrt(x * x + y * y)
        x *= invLength
        y *= invLength
        return this
    }

    fun normalize(dest: Vec2d): Vec2d {
        val invLength = Math.invsqrt(x * x + y * y)
        dest.x = x * invLength
        dest.y = y * invLength
        return dest
    }

    fun copy(): Vec2d {
        return Vec2d(this.x, this.y)
    }

    fun to2i() = Vec2i(x.i, y.i)
    fun to2f() = Vec2f(x.f, y.f)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putDouble("x", x)
        compound.putDouble("y", y)

        return compound
    }
}