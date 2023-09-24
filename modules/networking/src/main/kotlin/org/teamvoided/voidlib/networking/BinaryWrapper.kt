package org.teamvoided.voidlib.networking

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.EmptySerializersModule
import java.nio.charset.Charset

class BinaryWrapper(val format: StringFormat, val charset: Charset = Charsets.UTF_8): BinaryFormat {
    override val serializersModule = EmptySerializersModule()

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        return format.decodeFromString(deserializer, bytes.toString(charset))
    }

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        return format.encodeToString(serializer, value).toByteArray(charset)
    }
}