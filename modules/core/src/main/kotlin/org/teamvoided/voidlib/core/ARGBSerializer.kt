package org.teamvoided.voidlib.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(ARGB::class)
@OptIn(ExperimentalSerializationApi::class)
class ARGBSerializer: KSerializer<ARGB> {
    override fun serialize(encoder: Encoder, value: ARGB) {
        encoder.encodeInt(value.toInt())
    }

    override fun deserialize(decoder: Decoder): ARGB {
        return ARGB.fromArgbInt(decoder.decodeInt())
    }
}