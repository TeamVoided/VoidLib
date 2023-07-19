package org.teamvoided.voidlib.vui.v2.shader

import net.minecraft.client.render.VertexFormats
import org.teamvoided.voidlib.core.id

object Shaders {
    val hsvProgram = GlProgram(id("vui", "hsv"), VertexFormats.POSITION_COLOR)
}