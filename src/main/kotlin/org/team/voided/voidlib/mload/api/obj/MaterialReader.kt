package org.team.voided.voidlib.mload.api.obj

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import org.team.voided.voidlib.mload.api.obj.MaterialReader.Option
import java.io.BufferedReader
import java.io.IOException
import java.util.*


object MaterialReader {
    private val OPTIONS: MutableMap<String, Option> = HashMap()
    private val NONE = Option { _, _, _, _ -> }

    init {
        register(
            { _: StringTokenizer, line: String, key: String, material: MLoadMaterial ->
                material.setTexture(
                    Identifier(line.substring(key.length).trim { it <= ' ' })
                )
            },
            "map_Kd", "texture"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setColor(
                    parseFloat(tokenizer.nextToken()),
                    parseFloat(tokenizer.nextToken()),
                    parseFloat(tokenizer.nextToken())
                )
            },
            "Kd"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setBlendMode(
                    BlendMode.valueOf(tokenizer.nextToken().uppercase())
                )
            },
            "blend_mode"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setColorIndex(
                    parseBoolean(tokenizer.nextToken())
                )
            },
            "color_index"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setDiffuseShading(
                    parseBoolean(tokenizer.nextToken())
                )
            },
            "diffuse_shading"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setAmbientOcclusion(
                    parseBoolean(tokenizer.nextToken())
                )
            },
            "ambient_occlusion", "ambientocclusion", "ao"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setColor(
                    parseInt(tokenizer.nextToken(), 16)
                )
            },
            "diffuse_color", "color"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setEmissivity(
                    parseBoolean(tokenizer.nextToken())
                )
            },
            "emission", "emissive"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.lockUv(
                    parseBoolean(tokenizer.nextToken())
                )
            },
            "uvlock"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.setTintIndex(
                    parseInt(tokenizer.nextToken(), 10)
                )
            },
            "tint_index", "tintindex"
        )
        register(
            { tokenizer: StringTokenizer, _, _, material: MLoadMaterial ->
                material.cull(Direction.valueOf(tokenizer.nextToken().uppercase()))
            },
            "cull", "cullface"
        )
    }

    fun register(option: Option, vararg tokens: String) {
        for (key in tokens) {
            OPTIONS.putIfAbsent(key.lowercase(), option)
        }
    }

    @Throws(IOException::class)
    fun read(reader: BufferedReader): List<MLoadMaterial> {
        val materials: MutableList<MLoadMaterial> = ArrayList()
        var currentMaterial: MLoadMaterial? = null
        var line = reader.readLine()
        while (line != null) {
            val comment = line.indexOf('#')
            if (comment > 0) {
                line = line.substring(0, comment)
            }
            val tokenizer = StringTokenizer(line)
            if (!tokenizer.hasMoreTokens()) {
                line = next(reader)
                continue
            }
            val token = tokenizer.nextToken().lowercase()
            if (token == "newmtl") {
                val name = line.substring("newmtl".length).trim { it <= ' ' }
                currentMaterial = MLoadMaterial(name)
                materials.add(currentMaterial)
            }
            if (currentMaterial != null) {
                OPTIONS.getOrDefault(token, NONE).parse(tokenizer, line, token, currentMaterial)
            }
            line = next(reader)
        }
        return materials
    }

    @Throws(IOException::class)
    private fun next(reader: BufferedReader): String {
        var line = trim(reader.readLine())

        val result = StringBuilder(line)
        while (line.endsWith("\\")) {
            line = trim(reader.readLine())
            result.append(" ").append(line)
        }
        return result.toString()
    }

    private fun trim(line: String): String {
        return line.trim { it <= ' ' }
    }

    @Throws(IOException::class)
    private fun parseFloat(s: String): Float {
        return try {
            s.toFloat()
        } catch (e: NumberFormatException) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    private fun parseInt(s: String, radix: Int): Int {
        var str = s
        if (radix == 16 && str.startsWith("0x")) {
            str = str.substring(2)
        }
        return try {
            str.toInt(radix)
        } catch (e: NumberFormatException) {
            throw IOException(e)
        }
    }

    private fun parseBoolean(s: String): Boolean {
        return s.equals("true", ignoreCase = true)
    }

    fun interface Option {
        @Throws(IOException::class)
        fun parse(tokenizer: StringTokenizer, line: String, key: String, material: MLoadMaterial)
    }
}