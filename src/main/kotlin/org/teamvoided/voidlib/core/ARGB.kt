package org.teamvoided.voidlib.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

@Serializable
data class ARGB(val alpha: Int, val red: Int, val green: Int, val blue: Int) {
    companion object Serializer {
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

    fun toInt() = (alpha shl 24) + (red shl 16) + (green shl 8) + (blue shl 0)
}