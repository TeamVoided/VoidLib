package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.*
import kotlin.math.ceil

@Serializable
data class Vec2f(var x: Float, var y: Float) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec2f {
            return Vec2f(compound.getFloat("x"), compound.getFloat("y"))
        }
    }

    operator fun unaryPlus() = Vec2f(+x, +y)
    operator fun unaryMinus() = Vec2f(-x, -y)

    operator fun inc(): Vec2f {
        this.x++
        this.y++
        return this
    }

    operator fun dec(): Vec2f {
        this.x--
        this.y--
        return this
    }

    operator fun plus(toAdd: Vec2f): Vec2f = Vec2f(x = (this.x + toAdd.x), y = (this.y + toAdd.y))
    operator fun plus(toAdd: Number): Vec2f = Vec2f(x = (this.x + toAdd).f, y = (this.y + toAdd).f)

    operator fun minus(toSubtract: Vec2f): Vec2f = Vec2f(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y))
    operator fun minus(toSubtract: Number): Vec2f = Vec2f(x = (this.x - toSubtract).f, y = (this.y - toSubtract).f)

    operator fun times(toMultiply: Vec2f): Vec2f = Vec2f(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y))
    operator fun times(toMultiply: Number): Vec2f = Vec2f(x = (this.x * toMultiply.d).f, y = (this.y * toMultiply.d).f)

    operator fun div(toDivide: Vec2f): Vec2f = Vec2f(x = (this.x / toDivide.x), y = (this.y / toDivide.y))
    operator fun div(toDivide: Number): Vec2f = Vec2f(x = (this.x / toDivide).f, y = (this.y / toDivide).f)

    operator fun rem(toRem: Vec2f): Vec2f = Vec2f(x = (this.x % toRem.x), y = (this.y % toRem.y))
    operator fun rem(toRem: Number): Vec2f = Vec2f(x = (this.x % toRem).f, y = (this.y % toRem).f)

    operator fun contains(int: Float): Boolean = this.x == int || this.y == int

    operator fun compareTo(toCompare: Vec2f): Int {
        val totalValThis = this.x + this.y
        val totalValCompare = toCompare.x + toCompare.y

        return ceil(totalValThis - totalValCompare).i
    }

    fun add(x: Int, y: Int): Vec2f = Vec2f(x = this.x + x, y = this.y + y)
    fun addAssign(x: Int, y: Int) {
        this.x += x
        this.y += y
    }

    fun perpendicular(): Vec2f {
        val xTemp = y
        y = x * -1
        x = xTemp
        return this
    }

    fun normalize(): Vec2f {
        val invLength = Math.invsqrt(x * x + y * y)
        x *= invLength
        y *= invLength
        return this
    }

    fun normalize(dest: Vec2f): Vec2f {
        val invLength = Math.invsqrt(x * x + y * y)
        dest.x = x * invLength
        dest.y = y * invLength
        return dest
    }

    fun copy(): Vec2f {
        return Vec2f(this.x, this.y)
    }

    fun to2i() = Vec2i(x.i, y.i)
    fun to2d() = Vec2d(x.d, y.d)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putFloat("x", x)
        compound.putFloat("y", y)

        return compound
    }
}