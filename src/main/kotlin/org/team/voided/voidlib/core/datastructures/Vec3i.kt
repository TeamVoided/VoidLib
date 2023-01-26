package org.team.voided.voidlib.core.datastructures

data class Vec3i(var x: Int, var y: Int, var z: Int) {
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

    operator fun plus(toAdd: Vec3i): Vec3i =
        Vec3i(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z))

    operator fun plusAssign(toAdd: Vec3i) {
        this.x += toAdd.x
        this.y += toAdd.y
        this.z += toAdd.z
    }

    operator fun minus(toSubtract: Vec3i): Vec3i =
        Vec3i(x = (this.x + toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z))

    operator fun minusAssign(toSubtract: Vec3i) {
        this.x -= toSubtract.x
        this.y -= toSubtract.y
        this.z -= toSubtract.z
    }

    operator fun times(toMultiply: Vec3i): Vec3i =
        Vec3i(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z))

    operator fun timesAssign(toMultiply: Vec3i) {
        this.x *= toMultiply.x
        this.y *= toMultiply.y
        this.z *= toMultiply.z
    }

    operator fun div(toDivide: Vec3i): Vec3i =
        Vec3i(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z))

    operator fun divAssign(toDivide: Vec3i) {
        this.x /= toDivide.x
        this.y /= toDivide.y
        this.z /= toDivide.z
    }

    operator fun rem(toRem: Vec3i): Vec3i =
        Vec3i(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z))

    operator fun remAssign(toRem: Vec3i) {
        this.x %= toRem.x
        this.y %= toRem.y
        this.z %= toRem.z
    }

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

    fun copy(): Vec3i {
        return Vec3i(this.x, this.y, this.z)
    }
}
