package org.teamvoided.voidlib.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import kotlin.io.path.Path

open class ServerConfig(val id: Identifier) : SavableConfig<ServerConfig> {
    private val values: MutableMap<String, JsonElement> = LinkedHashMap()

    companion object {
        fun fromJson(id: Identifier): ServerConfig? {
            val config = ServerConfig(id)

            val file: File = Path("config/server/${id.namespace}/${id.path}.json").toFile()

            if (Files.exists(file.toPath())) {
                val reader = FileReader(file)

                val json: JsonObject = reader.use {
                    Gson().fromJson(it, JsonObject().javaClass)
                }

                json.entrySet().forEach { entry ->
                    config.setValue(entry.key, entry.value)
                }

                return config
            }

            return null
        }
    }

    override fun setValue(id: String, value: JsonElement): ServerConfig {
        values[id] = value
        return this
    }

    override fun getValue(id: String): JsonElement? {
        return values[id]
    }

    override fun removeValue(id: String): ServerConfig {
        values.remove(id)
        return this
    }

    operator fun set(id: String, value: JsonElement) {
        setValue(id, value)
    }

    operator fun get(id: String): JsonElement? {
        return getValue(id)
    }

    override fun save(): ServerConfig {
        val json = JsonObject()
        val file: File = Path("config/server/${id.namespace}/${id.path}.json").toFile()

        if (Files.notExists(file.toPath())) {
            Files.createDirectories(file.parentFile.toPath())
            Files.createFile(file.toPath())
        }

        values.forEach { (id, value) ->
            json.add(id, value)
        }

        FileWriter(file).use {
            it.write(
                GsonBuilder()
                    .setPrettyPrinting().create()
                    .toJson(json)
            )
        }

        return this
    }

    override fun load(): ServerConfig {
        val other: ServerConfig = fromJson(id) ?: return this

        values.forEach { (str, _) ->
            values.remove(str)
        }

        values.putAll(other.values)

        return this
    }

    fun addServerShutdownHook(): ServerConfig {
        ServerLifecycleEvents.SERVER_STOPPING.register { save() }
        return this
    }

    fun addServerStartupHook(): ServerConfig {
        ServerLifecycleEvents.SERVER_STARTING.register { load() }
        return this
    }
}