package org.team.voided.voidlib.wfc.wave.rule

import com.google.gson.JsonObject
import org.team.voided.voidlib.wfc.data.JsonType

class TileGenerationRuleType<T: ITileGenerationRule<T>>(val jsonParameters: List<Pair<String, JsonType>>, val constructor: (json: JsonObject) -> T) {
    fun construct(json: JsonObject): T {
        return constructor(json)
    }
}