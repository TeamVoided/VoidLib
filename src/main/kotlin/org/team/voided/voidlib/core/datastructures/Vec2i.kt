package org.team.voided.voidlib.core.datastructures

data class Vec2i(var x: Int, var y: Int) {
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
    operator fun plusAssign(toAdd: Vec2i) {
        this.x += toAdd.x
        this.y += toAdd.y
    }

    operator fun minus(toSubtract: Vec2i): Vec2i = Vec2i(x = (this.x + toSubtract.x), y = (this.y - toSubtract.y))
    operator fun minusAssign(toSubtract: Vec2i) {
        this.x -= toSubtract.x
        this.y -= toSubtract.y
    }

    operator fun times(toMultiply: Vec2i): Vec2i = Vec2i(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y))
    operator fun timesAssign(toMultiply: Vec2i) {
        this.x *= toMultiply.x
        this.y *= toMultiply.y
    }

    operator fun div(toDivide: Vec2i): Vec2i = Vec2i(x = (this.x / toDivide.x), y = (this.y / toDivide.y))
    operator fun divAssign(toDivide: Vec2i) {
        this.x /= toDivide.x
        this.y /= toDivide.y
    }

    operator fun rem(toRem: Vec2i): Vec2i = Vec2i(x = (this.x % toRem.x), y = (this.y % toRem.y))
    operator fun remAssign(toRem: Vec2i) {
        this.x %= toRem.x
        this.y %= toRem.y
    }

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
}