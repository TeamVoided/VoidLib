package org.team.voided.voidlib.mload.api.obj

import net.fabricmc.fabric.api.renderer.v1.Renderer
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction


class MLoadMaterial(val name: String) {
    companion object {
        @JvmStatic val DEFAULT: MLoadMaterial = MLoadMaterial("missing_texture")

        init {
            DEFAULT.setTexture(Identifier(""))
        }
    }

    private var tintIndex = -1
    private var color = -1
    private var texture: Identifier? = null
    private var blendMode = BlendMode.DEFAULT

    private var uvLocked = false
    private var diffuseShading = true
    private var ambientOcclusion = true
    private var emission = false
    private var colorIndex = true

    private var material: RenderMaterial? = null
    private var cullDirection: Direction? = null

    fun setTexture(textureId: Identifier?) {
        texture = textureId
    }

    fun setColor(kd0: Float, kd1: Float, kd2: Float) {
        color = -0x1000000
        color = color or ((255 * kd0).toInt() shl 16)
        color = color or ((255 * kd1).toInt() shl 8)
        color = color or (255 * kd2).toInt()
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun getColor(): Int {
        return color
    }

    fun getTintIndex(): Int {
        return tintIndex
    }

    fun setTintIndex(tintIndex: Int) {
        this.tintIndex = tintIndex
    }

    fun getTexture(): Identifier? {
        return texture
    }

    fun lockUv(enabled: Boolean) {
        uvLocked = enabled
    }

    fun isUvLocked(): Boolean {
        return uvLocked
    }

    fun setBlendMode(blendMode: BlendMode) {
        this.blendMode = blendMode
    }

    fun setAmbientOcclusion(enabled: Boolean) {
        ambientOcclusion = enabled
    }

    fun setEmissivity(enabled: Boolean) {
        emission = enabled
    }

    fun setDiffuseShading(enabled: Boolean) {
        diffuseShading = enabled
    }

    fun setColorIndex(enabled: Boolean) {
        colorIndex = enabled
    }

    fun cull(direction: Direction?) {
        cullDirection = direction
    }

    fun getCullDirection(): Direction? {
        return cullDirection
    }

    fun getMaterial(renderer: Renderer): RenderMaterial? {
        if (material == null) {
            val finder: MaterialFinder = renderer.materialFinder()
                .blendMode(0, blendMode)
                .disableAo(0, !ambientOcclusion)
                .emissive(0, emission)
                .disableDiffuse(0, !diffuseShading)
                .disableColorIndex(0, !colorIndex)
            material = finder.find()
        }
        return material
    }

    override fun hashCode(): Int {
        return if (texture == null) {
            name.hashCode()
        } else {
            texture.hashCode()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MLoadMaterial

        if (name != other.name) return false
        if (tintIndex != other.tintIndex) return false
        if (color != other.color) return false
        if (texture != other.texture) return false
        if (blendMode != other.blendMode) return false
        if (uvLocked != other.uvLocked) return false
        if (diffuseShading != other.diffuseShading) return false
        if (ambientOcclusion != other.ambientOcclusion) return false
        if (emission != other.emission) return false
        if (colorIndex != other.colorIndex) return false
        if (material != other.material) return false
        if (cullDirection != other.cullDirection) return false

        return true
    }
}