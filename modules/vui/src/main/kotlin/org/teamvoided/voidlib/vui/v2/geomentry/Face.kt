package org.teamvoided.voidlib.vui.v2.geomentry

import kotlinx.serialization.Serializable
import org.teamvoided.voidlib.core.datastructures.vector.Vec3f
import org.teamvoided.voidlib.core.datastructures.vector.Vec4d

@Serializable
data class Face(val normals: List<Vec3f>, val vertices: List<Vec3f>, val colors: List<Vec4d>) {
    val hasColor = colors.isNotEmpty()
}