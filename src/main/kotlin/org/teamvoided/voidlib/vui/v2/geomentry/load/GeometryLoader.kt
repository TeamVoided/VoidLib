package org.teamvoided.voidlib.vui.v2.geomentry.load

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.metadata.ResourceMetadata
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.vui.v2.geomentry.Geometry
import java.io.BufferedReader
import java.io.InputStream
import java.nio.ByteBuffer

interface GeometryLoader {
    fun canLoad(id: Identifier, manager: ResourceManager): Boolean

    fun load(id: Identifier, manager: ResourceManager): Geometry?

    fun parseJson(id: Identifier, manager: ResourceManager): JsonObject {
        return getGson().fromJson(getResourceFileReader(id, manager), JsonObject::class.java)
    }

    fun getResourceFile(id: Identifier, manager: ResourceManager): InputStream {
        return getResource(id, manager).inputStream
    }

    fun getResourceByteBuffer(id: Identifier, manager: ResourceManager): ByteBuffer {
        val stream = getResourceFile(id, manager)
        val bytes = stream.readAllBytes()
        stream.close()

        return ByteBuffer.wrap(bytes)
    }

    fun getResourceFileReader(id: Identifier, manager: ResourceManager): BufferedReader {
        return getResource(id, manager).reader
    }

    fun getResourceMetaData(id: Identifier, manager: ResourceManager): ResourceMetadata {
        return getResource(id, manager).metadata
    }

    fun getResourcePackName(id: Identifier, manager: ResourceManager): String {
        return getResource(id, manager).resourcePackName
    }

    fun getResource(id: Identifier, manager: ResourceManager): Resource {
        return manager.getResource(id).get()
    }

    fun getGson(): Gson {
        return Gson()
    }
}