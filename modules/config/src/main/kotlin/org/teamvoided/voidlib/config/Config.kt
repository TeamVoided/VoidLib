package org.teamvoided.voidlib.config

import com.google.gson.JsonElement

interface Config<E : Config<E>> : ImmutableConfig {
    fun setValue(id: String, value: JsonElement): E
    fun removeValue(id: String): E
}