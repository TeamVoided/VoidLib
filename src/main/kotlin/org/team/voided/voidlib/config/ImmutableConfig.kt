package org.team.voided.voidlib.config

import com.google.gson.JsonElement

interface ImmutableConfig {
    fun getValue(id: String): JsonElement?
}