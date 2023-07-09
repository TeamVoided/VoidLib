package org.teamvoided.voidlib.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable
import net.minecraft.util.math.MathHelper

@Serializable
data class ARGB(var alpha: Int, var red: Int, var green: Int, var blue: Int) {
    companion object Serializer {
        fun fromHexString(hex: String) = fromArgbInt(hex.toInt(16))

        fun fromHSV(hue: Float, saturation: Float, value: Float, alpha: Float) =
            fromArgbInt((alpha * 255).i shl 24 or MathHelper.hsvToRgb(hue - .5e-7f, saturation, value))

        fun fromArgbInt(argb: Int) = ARGB(argb ushr 24, argb shr 16 and 0xFF, argb shr 8 and 0xFF, argb and 0xFF)

        fun toJson(rgba: ARGB): JsonObject {
            val json = JsonObject()
            json.addProperty("r", rgba.red)
            json.addProperty("g", rgba.green)
            json.addProperty("b", rgba.blue)
            json.addProperty("a", rgba.alpha)

            return json
        }

        fun fromJson(json: JsonObject): ARGB {
            return ARGB(json.get("r").asInt, json.get("g").asInt, json.get("b").asInt, json.get("a").asInt)
        }

        fun fromJson(json: String): ARGB {
            val gson = Gson()
            return fromJson(gson.fromJson(json, JsonObject::class.java))
        }
    }

    constructor(red: Int, green: Int, blue: Int): this(255, red, green, blue)

    fun toInt() = ((alpha shl 24) + (red shl 16) + (green shl 8) + (blue shl 0))
}