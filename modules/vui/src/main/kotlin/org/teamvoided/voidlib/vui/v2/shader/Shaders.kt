package org.teamvoided.voidlib.vui.v2.shader

import net.minecraft.client.render.VertexFormats
import org.teamvoided.voidlib.core.LOGGER
import org.teamvoided.voidlib.core.id

object Shaders {
    private lateinit var internalHsvProgram: GlProgram
    val hsvProgram get() = internalHsvProgram


    fun init() {
        internalHsvProgram = GlProgram(id("hsv"), VertexFormats.POSITION_COLOR)
    }
}