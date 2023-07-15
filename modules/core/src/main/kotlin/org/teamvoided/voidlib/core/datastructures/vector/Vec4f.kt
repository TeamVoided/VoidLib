package org.teamvoided.voidlib.core.datastructures.vector

import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound
import org.joml.Math
import org.joml.Matrix4fc
import org.teamvoided.voidlib.core.*
import kotlin.math.ceil


@Serializable
data class Vec4f(var x: Float, var y: Float, var z: Float, var w: Float) {
    companion object {
        fun fromNbt(compound: NbtCompound): Vec4f {
            return Vec4f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"), compound.getFloat("w"))
        }
    }

    operator fun unaryPlus() = Vec4f(+x, +y, +z, +w)
    operator fun unaryMinus() = Vec4f(-x, -y, +z, +w)

    operator fun inc(): Vec4f {
        this.x++
        this.y++
        this.z++
        this.w++
        return this
    }

    operator fun dec(): Vec4f {
        this.x--
        this.y--
        this.z--
        this.w--
        return this
    }

    operator fun plus(toAdd: Vec4f): Vec4f = Vec4f(x = (this.x + toAdd.x), y = (this.y + toAdd.y), z = (this.z + toAdd.z), w = (this.w + toAdd.w))
    operator fun plus(toAdd: Number): Vec4f = Vec4f(x = (this.x + toAdd).f, y = (this.y + toAdd).f, z = (this.z + toAdd).f, w = (this.w + toAdd).f)

    operator fun minus(toSubtract: Vec4f): Vec4f = Vec4f(x = (this.x - toSubtract.x), y = (this.y - toSubtract.y), z = (this.z - toSubtract.z), w = (this.w - toSubtract.w))
    operator fun minus(toSubtract: Number): Vec4f = Vec4f(x = (this.x - toSubtract).f, y = (this.y - toSubtract).f, z = (this.z - toSubtract).f, w = (this.w - toSubtract).f)

    operator fun times(toMultiply: Vec4f): Vec4f = Vec4f(x = (this.x * toMultiply.x), y = (this.y * toMultiply.y), z = (this.z * toMultiply.z), w = (this.w * toMultiply.w))
    operator fun times(toMultiply: Number): Vec4f = Vec4f(x = (this.x * toMultiply).f, y = (this.y * toMultiply).f, z = (this.z * toMultiply).f, w = (this.w * toMultiply).f)
    operator fun times(mat: Matrix4fc): Vec4f = if (mat.properties() and Matrix4fc.PROPERTY_AFFINE.i != 0) timesAffine(mat) else timesGeneric(mat)
    fun timesAffine(mat: Matrix4fc): Vec4f {
        x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w)))
        y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w)))
        z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w)))
        return this
    }

    private fun timesGeneric(mat: Matrix4fc): Vec4f {
        timesAffine(mat)
        w = Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)))
        return this
    }

    operator fun div(toDivide: Vec4f): Vec4f = Vec4f(x = (this.x / toDivide.x), y = (this.y / toDivide.y), z = (this.z / toDivide.z), w = (this.w / toDivide.w))
    operator fun div(toDivide: Number): Vec4f = Vec4f(x = (this.x / toDivide).f, y = (this.y / toDivide).f, z = (this.z / toDivide).f, w = (this.w / toDivide).f)

    operator fun rem(toRem: Vec4f): Vec4f = Vec4f(x = (this.x % toRem.x), y = (this.y % toRem.y), z = (this.z % toRem.z), w = (this.w % toRem.w))
    operator fun rem(toRem: Number): Vec4f = Vec4f(x = (this.x % toRem).f, y = (this.y % toRem).f, z = (this.z % toRem).f, w = (this.w % toRem).f)

    operator fun contains(int: Float): Boolean = this.x == int || this.y == int || this.z == int || this.w == int

    operator fun compareTo(toCompare: Vec4f): Int {
        val totalValThis = this.x + this.y + this.z + this.w
        val totalValCompare = toCompare.x + toCompare.y + toCompare.z + this.w

        return ceil(totalValThis - totalValCompare).i
    }

    fun add(x: Int, y: Int, z: Int, w: Int): Vec4f = Vec4f(x = this.x + x, y = this.y + y, this.z + z, this.w + w)
    fun addAssign(x: Int, y: Int, z: Int, w: Int) {
        this.x += x
        this.y += y
        this.z += z
        this.w += w
    }

    fun normalize(): Vec4f {
        val invLength = 1f / length()
        x *= invLength
        y *= invLength
        z *= invLength
        w *= invLength
        return this
    }

    fun normalize(dest: Vec4f): Vec4f {
        val invLength = 1f / length()
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        dest.w = w * invLength
        return dest
    }

    fun normalize(length: Float): Vec4f {
        val invLength: Float = 1f / length() * length
        x *= invLength
        y *= invLength
        z *= invLength
        w *= invLength
        return this
    }

    fun normalize(length: Float, dest: Vec4f): Vec4f {
        val invLength = 1f / length() * length
        dest.x = x * invLength
        dest.y = y * invLength
        dest.z = z * invLength
        dest.w = w * invLength
        return dest
    }

    fun length(): Float {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, Math.fma(z, z, w * w))))
    }

    fun copy(): Vec4f {
        return Vec4f(this.x, this.y, this.z, this.w)
    }

    fun toARGB() = ARGB((x * 255).i, (y * 255).i, (z * 255).i, (w * 255).i)

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        compound.putFloat("x", x)
        compound.putFloat("y", y)
        compound.putFloat("z", z)
        compound.putFloat("w", w)

        return compound
    }
}
