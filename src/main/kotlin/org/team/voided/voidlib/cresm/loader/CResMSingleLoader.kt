package org.team.voided.voidlib.cresm.loader

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.metadata.ResourceMetadata
import net.minecraft.util.Identifier
import java.io.BufferedReader
import java.io.InputStream

interface CResMSingleLoader {
    fun loadResource(id: Identifier, manager: ResourceManager)

    fun parseJson(id: Identifier, manager: ResourceManager): JsonObject {
        return getGson().fromJson(getResourceFileReader(id, manager), JsonObject::class.java)
    }

    fun getResourceFile(id: Identifier, manager: ResourceManager): InputStream {
        return getResource(id, manager).inputStream
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