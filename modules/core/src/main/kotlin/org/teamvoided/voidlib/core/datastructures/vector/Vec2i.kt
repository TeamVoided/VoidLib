package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.teamvoided.voidlib.core.*

@Serializable
data class Vec2i(var x: Int, var y: Int) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec2i {
            return Vec2i(compound.getInt("x"), compound.getInt("y"))
        }
    }

    operator fun unaryPlus() = Vec2i(+x, +y)
    operator fun unaryMinus() = Vec2i(-x, -y)

    operator fun inc(): Vec2i {
        this.x++
        this.y++
        return this
    }

    operator fun dec(): Vec2i {
        this.x--
        this.y--
        return this
    }

    operator fun plus(toAdd: Vec2i): Vec2i = Vec2i(x = (this.x + toAdd.x), y = (this.y + toAdd.y))
    operator fun plus(toAdd: Number): Vec2i = Vec2i(x = (this.x + toAdd.d).i, y = (this.y + toAdd.d).i)

    operator fun minus(toSubtract: Vec2i): Vec2i = Vec2i(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y))
    operator fun minus(toSubtract: Number): Vec2i = Vec2i(x = (this.x - toSubtract.d).i, y = (this.y - toSubtract.d).i)

    operator fun times(toMultiply: Vec2i): Vec2i = Vec2i(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y))
    operator fun times(toMultiply: Number): Vec2i = Vec2i(x = (this.x * toMultiply.d).i, y = (this.y * toMultiply.d).i)

    operator fun div(toDivide: Vec2i): Vec2i = Vec2i(x = (this.x / toDivide.x), y = (this.y / toDivide.y))
    operator fun div(toDivide: Number): Vec2i = Vec2i(x = (this.x / toDivide).i, y = (this.y / toDivide.d).i)

    operator fun rem(toRem: Vec2i): Vec2i = Vec2i(x = (this.x % toRem.x), y = (this.y % toRem.y))
    operator fun rem(toRem: Number): Vec2i = Vec2i(x = (this.x % toRem).i, y = (this.y % toRem).i)

    operator fun contains(int: Int): Boolean = this.x == int || this.y == int

    operator fun compareTo(toCompare: Vec2i): Int {
        val totalValThis = this.x + this.y
        val totalValCompare = toCompare.x + toCompare.y

        return (totalValThis - totalValCompare)
    }
    fun add(x: Int, y: Int): Vec2i = Vec2i(x = this.x + x, y = this.y + y)

    fun addAssign(x: Int, y: Int) {
        this.x += x
        this.y += y
    }

    fun perpendicular(): Vec2i {
        val xTemp = y
        y = x * -1
        x = xTemp
        return this
    }

    fun normalize(): Vec2i {
        val invLength = Math.invsqrt((x * x + y * y).toDouble())
        x = (x * invLength).i
        y = (y * invLength).i
        return this
    }

    fun normalize(dest: Vec2i): Vec2i {
        val invLength = Math.invsqrt((x * x + y * y).toDouble())
        dest.x = (x * invLength).i
        dest.y = (y * invLength).i
        return dest
    }

    fun copy(): Vec2i {
        return Vec2i(this.x, this.y)
    }
    fun to2f() = Vec2f(x.f, y.f)

    fun to2d() = Vec2d(x.d, y.d)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putInt("x", x)
        compound.putInt("y", y)

        return compound
    }
}