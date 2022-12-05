package org.team.voided.voidlib.config

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.util.Identifier
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import kotlin.io.path.Path

class ServerConfig(id: Identifier) : Config<ServerConfig>(id) {
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
                    config.setValue(entry.key, ConfigValue.fromJson(entry.value.asJsonObject)!!)
                }

                return config
            }

            return null
        }
    }

    override fun setValue(id: String, value: ConfigValue): ServerConfig {
        values[id] = value
        return this
    }

    override fun removeValue(id: String): ServerConfig {
        values.remove(id)
        return this
    }

    override fun save(): ServerConfig {
        val json = JsonObject()
        val file: File = Path("config/server/${id.namespace}/${id.path}.json").toFile()

        if (Files.notExists(file.toPath())) {
            Files.createDirectories(file.parentFile.toPath())
            Files.createFile(file.toPath())
        }

        values.forEach { (id, value) ->
            json.add(id, value.toJson())
        }

        FileWriter(file).use {
            it.write(Gson().toJson(json))
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