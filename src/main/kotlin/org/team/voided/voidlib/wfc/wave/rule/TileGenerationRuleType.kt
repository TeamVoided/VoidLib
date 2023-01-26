package org.team.voided.voidlib.wfc.wave.rule

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.team.voided.voidlib.wfc.data.JsonType

class TileGenerationRuleType<T : ITileGenerationRule<T>>(
    private val jsonParameters: List<Pair<String, JsonType>>,
    private val constructor: (json: JsonObject) -> T
) {
    fun construct(json: JsonObject): T {
        jsonParameters.forEach {
            if (json.has(it.first)) {
                val type = json.get(it.first).getType()
                if (type != it.second)
                    throw InvalidPropertyTypeException(it.first, it.second, type)
            } else {
                throw MissingPropertyException(it.first, it.second)
            }
        }

        return constructor(json)
    }

    private fun <T : JsonElement> T.getType(): JsonType {
        if (isJsonArray) return JsonType.JSON_ARRAY
        if (isJsonObject) return JsonType.JSON_OBJECT
        if (isJsonNull) return JsonType.JSON_NULL
        if (asJsonPrimitive.isNumber) return JsonType.NUMBER
        if (asJsonPrimitive.isBoolean) return JsonType.BOOLEAN
        if (asJsonPrimitive.isString) return JsonType.STRING

        return JsonType.JSON_NULL
    }
}