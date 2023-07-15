package org.teamvoided.voidlib.vui.v2.geomentry

import kotlinx.serialization.Serializable
import net.minecraft.client.render.VertexFormat.DrawMode
import org.teamvoided.voidlib.core.datastructures.vector.Vec3f
import org.teamvoided.voidlib.core.datastructures.vector.Vec4d
import java.util.stream.Collectors

@Serializable
data class Geometry(val type: Type, val faces: List<Face>) {
    val vertices: List<Vec3f> = faces.map { it.vertices }.stream().flatMap { it.stream() }.collect(Collectors.toList())
    val colors: List<Vec4d> = faces.map { it.colors }.stream().flatMap { it.stream() }.collect(Collectors.toList())
    val normals: List<Vec3f> = faces.map { it.normals }.stream().flatMap { it.stream() }.collect(Collectors.toList())

    enum class Type(val drawMode: DrawMode) {
        TRIANGLES(DrawMode.TRIANGLES),
        QUADS(DrawMode.QUADS)
    }
}