package org.team.voided.voidlib.mload.impl

import com.google.gson.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.model.ModelProviderContext
import net.fabricmc.fabric.api.client.model.ModelResourceProvider
import net.fabricmc.fabric.api.client.model.ModelVariantProvider
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.render.model.json.Transformation
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.math.Vec3f
import org.team.voided.voidlib.mload.MLoadImpl
import org.team.voided.voidlib.mload.api.obj.ObjLoader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.lang.reflect.Type


class ObjLoaderImpl(val manager: ResourceManager): ObjLoader(), ModelResourceProvider, ModelVariantProvider {
    companion object {
        private val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(ModelTransformation::class.java, ModelTransformDeserializer())
            .registerTypeAdapter(Transformation::class.java, TransformDeserializer())
            .create()

    }

    override fun loadModelResource(
        id: Identifier,
        modelProviderContext: ModelProviderContext
    ): UnbakedModel? {
        return loadObj(this.manager, id, ModelTransformation.NONE, true)
    }

    override fun loadModelVariant(
        modelIdentifier: ModelIdentifier,
        modelProviderContext: ModelProviderContext
    ): UnbakedModel? {
        val resource = Identifier(
            modelIdentifier.namespace,
            "models/item/${modelIdentifier.path}.json"
        )
        if (!modelIdentifier.variant.equals("inventory") || this.manager.getResource(resource).isEmpty) {
            return null
        }
        try {
            InputStreamReader(this.manager.getResource(resource).get().inputStream).use { reader ->
                val rawModel: JsonObject = JsonHelper.deserialize(reader)
                val model: JsonElement = rawModel.get("model")
                if (model !is JsonPrimitive || !model.isString) {
                    return null
                }
                val modelPath = Identifier(model.getAsString())
                val transformation: ModelTransformation =
                    this.getTransformation(rawModel)
                var isSideLit = true
                if (rawModel.has("gui_light")) {
                    isSideLit = JsonHelper.getString(rawModel, "gui_light") == "side"
                }
                return this.loadObj(this.manager, modelPath, transformation, isSideLit)
            }
        } catch (ex: IOException) {
            MLoadImpl.LOGGER.warn("Failed to load model {}:\n{}", resource, ex.message)
            return null
        }
    }

    @Throws(IOException::class)
    private fun getTransformation(rawModel: JsonObject): ModelTransformation {
        return if (rawModel.has("display")) {
            val rawTransform: JsonObject = JsonHelper.getObject(rawModel, "display")
            GSON.fromJson(rawTransform, ModelTransformation::class.java)
        } else if (rawModel.has("parent")) {
            var parent = Identifier(JsonHelper.getString(rawModel, "parent"))
            parent = Identifier(parent.namespace, "models/" + parent.path + ".json")
            this.getTransformation(parent)
        } else {
            ModelTransformation.NONE
        }
    }

    @Throws(IOException::class)
    private fun getTransformation(model: Identifier): ModelTransformation {
        return if (this.manager.getResource(model).isPresent) {
            val reader: Reader = InputStreamReader(this.manager.getResource(model).get().inputStream)
            getTransformation(JsonHelper.deserialize(reader))
        } else {
            ModelTransformation.NONE
        }
    }

    @Environment(EnvType.CLIENT)
    class ModelTransformDeserializer: JsonDeserializer<ModelTransformation> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            jsonElement: JsonElement,
            type: Type,
            jsonDeserializationContext: JsonDeserializationContext
        ): ModelTransformation {
            val jsonObject = jsonElement.asJsonObject
            val transformation = parseModelTransformation(jsonDeserializationContext, jsonObject, "thirdperson_righthand")

            var transformation2 = parseModelTransformation(jsonDeserializationContext, jsonObject, "thirdperson_lefthand")
            if (transformation2 === Transformation.IDENTITY) {
                transformation2 = transformation
            }

            val transformation3 = parseModelTransformation(jsonDeserializationContext, jsonObject, "firstperson_righthand")

            var transformation4 = parseModelTransformation(jsonDeserializationContext, jsonObject, "firstperson_lefthand")
            if (transformation4 === Transformation.IDENTITY) {
                transformation4 = transformation3
            }
            val transformation5 = parseModelTransformation(jsonDeserializationContext, jsonObject, "head")
            val transformation6 = parseModelTransformation(jsonDeserializationContext, jsonObject, "gui")
            val transformation7 = parseModelTransformation(jsonDeserializationContext, jsonObject, "ground")
            val transformation8 = parseModelTransformation(jsonDeserializationContext, jsonObject, "fixed")

            return ModelTransformation(
                transformation2,
                transformation,
                transformation4,
                transformation3,
                transformation5,
                transformation6,
                transformation7,
                transformation8
            )
        }

        private fun parseModelTransformation(
            ctx: JsonDeserializationContext,
            json: JsonObject,
            key: String
        ): Transformation {
            return if (json.has(key)) ctx.deserialize<Any>(
                json[key],
                Transformation::class.java
            ) as Transformation else Transformation.IDENTITY
        }
    }

    @Environment(EnvType.CLIENT)
    class TransformDeserializer: JsonDeserializer<Transformation> {
        companion object {
            private val DEFAULT_ROTATION = Vec3f(0.0f, 0.0f, 0.0f)
            private val DEFAULT_TRANSLATION = Vec3f(0.0f, 0.0f, 0.0f)
            private val DEFAULT_SCALE = Vec3f(1.0f, 1.0f, 1.0f)
            const val field_32808 = 5.0f
            const val field_32809 = 4.0f
        }

        @Throws(JsonParseException::class)
        override fun deserialize(
            jsonElement: JsonElement,
            type: Type,
            jsonDeserializationContext: JsonDeserializationContext
        ): Transformation {
            val jsonObject = jsonElement.asJsonObject
            val vec3f = parseVector3f(jsonObject, "rotation", DEFAULT_ROTATION)
            val vec3f2 = parseVector3f(jsonObject, "translation", DEFAULT_TRANSLATION)
            vec3f2.scale(0.0625f)
            vec3f2.clamp(-5.0f, 5.0f)
            val vec3f3 = parseVector3f(jsonObject, "scale", DEFAULT_SCALE)
            vec3f3.clamp(-4.0f, 4.0f)
            return Transformation(vec3f, vec3f2, vec3f3)
        }

        private fun parseVector3f(json: JsonObject, key: String, fallback: Vec3f): Vec3f {
            return if (!json.has(key)) {
                fallback
            } else {
                val jsonArray = JsonHelper.getArray(json, key)
                if (jsonArray.size() != 3) {
                    throw JsonParseException("Expected 3 " + key + " values, found: " + jsonArray.size())
                } else {
                    val fs = FloatArray(3)
                    for (i in fs.indices) {
                        fs[i] = JsonHelper.asFloat(jsonArray[i], "$key[$i]")
                    }
                    Vec3f(fs[0], fs[1], fs[2])
                }
            }
        }
    }
}