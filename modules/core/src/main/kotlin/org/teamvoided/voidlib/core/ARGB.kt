package org.teamvoided.voidlib.core

import kotlinx.serialization.Serializable
import net.minecraft.util.math.MathHelper

@Serializable(with = ARGBSerializer::class)
data class ARGB(var alpha: Int, var red: Int, var green: Int, var blue: Int) {
    companion object {
        fun fromHexString(hex: String) = fromArgbInt(hex.toInt(16))

        fun fromHSV(hue: Float, saturation: Float, value: Float, alpha: Float) =
            fromArgbInt((alpha * 255).i shl 24 or MathHelper.hsvToRgb(hue - .5e-7f, saturation, value))

        fun fromArgbInt(argb: Int) = ARGB(argb ushr 24, argb shr 16 and 0xFF, argb shr 8 and 0xFF, argb and 0xFF)

        val WHITE = ARGB(255, 255,255)
    }

    constructor(red: Int, green: Int, blue: Int): this(255, red, green, blue)

    fun toInt() = ((alpha shl 24) + (red shl 16) + (green shl 8) + (blue shl 0))
}