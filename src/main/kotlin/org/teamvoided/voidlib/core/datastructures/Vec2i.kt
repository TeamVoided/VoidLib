package org.teamvoided.voidlib.core.datastructures

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound

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

    operator fun minus(toSubtract: Vec2i): Vec2i = Vec2i(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y))

    operator fun times(toMultiply: Vec2i): Vec2i = Vec2i(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y))

    operator fun div(toDivide: Vec2i): Vec2i = Vec2i(x = (this.x / toDivide.x), y = (this.y / toDivide.y))

    operator fun rem(toRem: Vec2i): Vec2i = Vec2i(x = (this.x % toRem.x), y = (this.y % toRem.y))

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

    fun copy(): Vec2i {
        return Vec2i(this.x, this.y)
    }

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putInt("x", x)
        compound.putInt("y", y)

        return compound
    }
}