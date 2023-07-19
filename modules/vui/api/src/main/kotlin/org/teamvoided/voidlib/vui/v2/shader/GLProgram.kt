package org.teamvoided.voidlib.vui.v2.shader

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gl.GlUniform
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.VertexFormat
import net.minecraft.resource.ResourceFactory
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import org.teamvoided.voidlib.vui.mixin.ShaderProgramAccessor
import java.io.IOException
import java.util.*


open class GlProgram(id: Identifier, vertexFormat: VertexFormat) {
    protected open lateinit var backingProgram: ShaderProgram

    companion object {
        private val REGISTERED_PROGRAMS: MutableList<Pair<(ResourceFactory) -> ShaderProgram, (ShaderProgram) -> Unit>> = LinkedList()

        @ApiStatus.Internal @JvmStatic
        fun forEachProgram(loader: (Pair<(ResourceFactory) -> ShaderProgram, (ShaderProgram) -> Unit>) -> Unit) {
            REGISTERED_PROGRAMS.forEach(loader)
        }
    }

    init {
        REGISTERED_PROGRAMS.add(Pair(program@
        { resourceFactory ->
            try {
                return@program VoidShaderProgram(resourceFactory, id.toString(), vertexFormat)
            } catch (e: IOException) {
                throw RuntimeException("Failed to initialize void shader program", e)
            }
        }, { program ->
                backingProgram = program
                setup()
            }
        ))
    }

    open fun use() {
        RenderSystem.setShader { backingProgram }
    }

    protected open fun setup() {}

    protected open fun findUniform(name: String): GlUniform {
        return (backingProgram as ShaderProgramAccessor).void_getLoadedUniforms()[name]!!
    }

    class VoidShaderProgram(factory: ResourceFactory, name: String, format: VertexFormat): ShaderProgram(factory, name, format)
}