package org.teamvoided.voidlib.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

@Serializable
data class ARGB(var alpha: UByte, var red: UByte, var green: UByte, var blue: UByte) {
    companion object Serializer {
        fun toJson(rgba: ARGB): JsonObject {
            val json = JsonObject()
            json.addProperty("r", rgba.red.toInt())
            json.addProperty("g", rgba.green.toInt())
            json.addProperty("b", rgba.blue.toInt())
            json.addProperty("a", rgba.alpha.toInt())

            return json
        }

        fun fromJson(json: JsonObject): ARGB {
            return ARGB(json.get("r").asInt.toUByte(), json.get("g").asInt.toUByte(), json.get("b").asInt.toUByte(), json.get("a").asInt.toUByte())
        }

        fun fromJson(json: String): ARGB {
            val gson = Gson()
            return fromJson(gson.fromJson(json, JsonObject::class.java))
        }
    }

    private infix fun UByte.shl(bitCount: Int): UInt = toUInt() shl bitCount

    fun toInt() = ((alpha shl 24) + (red shl 16) + (green shl 8) + (blue shl 0)).toInt()
}