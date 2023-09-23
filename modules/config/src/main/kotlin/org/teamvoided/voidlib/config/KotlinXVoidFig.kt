package org.teamvoided.voidlib.config

import kotlinx.serialization.*
import net.minecraft.util.Identifier
import kotlin.reflect.KClass

abstract class KotlinXVoidFig<Config: Any>(
    override val id: Identifier,
    override val side: Side,
    protected var config: Config,
    val configFormat: StringFormat,
    //Typically obtained from Class.serializer() if the class is @Serializable (kotlinx.serialization)
    val configSerializer: KSerializer<Config>,
    override val fileType: String
): VoidFig {

    @OptIn(ExperimentalSerializationApi::class)
    constructor(
        id: Identifier,
        side: Side,
        configKlass: KClass<Config>,
        config: Config,
        configFormat: StringFormat,
        fileType: String
    ): this(id, side, config, configFormat, ContextualSerializer(configKlass), fileType)

    protected fun serialize(data: Config): String {
        return configFormat.encodeToString(configSerializer, data)
    }

    override fun serialize() {
        val file = VoidFigHelpers.getConfigFile(id, side, fileType)
        file.mkdirs()
        file.createNewFile()

        file.writeText(serialize(config))
    }

    protected fun deserialize(data: String): Config {
        return configFormat.decodeFromString(configSerializer, data)
    }

    override fun deserialize() {
        val file = VoidFigHelpers.getConfigFile(id, side, fileType)
        if (!file.exists()) {
            serialize()
            return
        }

        config = deserialize(file.readText())
    }

    @JvmName("get_config")
    fun getConfig() = config
}