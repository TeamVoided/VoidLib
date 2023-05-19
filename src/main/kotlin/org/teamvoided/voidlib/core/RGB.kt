package org.teamvoided.voidlib.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

@Serializable
data class RGB(val red: Int, val green: Int, val blue: Int) {
    companion object Serializer {
        fun toJson(rgb: RGB): JsonObject {
            val json = JsonObject()
            json.addProperty("r", rgb.red)
            json.addProperty("g", rgb.green)
            json.addProperty("b", rgb.blue)

            return json
        }

        fun fromJson(json: JsonObject): RGB {
            return RGB(json.get("r").asInt, json.get("g").asInt, json.get("b").asInt)
        }

        fun fromJson(json: String): RGB {
            val gson = Gson()
            return fromJson(gson.fromJson(json, JsonObject::class.java))
        }
    }

    fun toInt() = red and 0x0ff shl 16 or (green and 0x0ff shl 8) or (blue and 0x0ff)
}