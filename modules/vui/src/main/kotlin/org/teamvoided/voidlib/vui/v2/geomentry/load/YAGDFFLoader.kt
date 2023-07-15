package org.teamvoided.voidlib.vui.v2.geomentry.load

import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.LOGGER
import org.teamvoided.voidlib.core.*
import org.teamvoided.voidlib.core.datastructures.ListPartition
import org.teamvoided.voidlib.core.datastructures.vector.Vec3f
import org.teamvoided.voidlib.core.datastructures.vector.Vec4d
import org.teamvoided.voidlib.vui.v2.geomentry.Face
import org.teamvoided.voidlib.vui.v2.geomentry.Geometry
import java.nio.ByteBuffer
import java.util.*

open class YAGDFFLoader: GeometryLoader {
    companion object {
        fun isYAGDFF(buffer: ByteBuffer): Boolean {
            return buffer startsWith "yagdff" || buffer startsWith "solid"
        }

        @Suppress("Duplicates")
        fun loadYAGDFF(buffer: ByteBuffer): Geometry? {
            var buf = String(ByteArray(buffer.position(0).remaining()))
            val normalBuffer = LinkedList<Vec3f>()
            val vertexBuffer = LinkedList<Vec3f>()
            val colorBuffer = LinkedList<Vec4d>()
            val faceBuffer = LinkedList<Face>()

            buf = buf.removePrefix("yagdff")
            buf = buf.removePrefix("solid")
            buf = buf.trim()

            val words = buf.words

            if (!buffer[0].c.isNewLine) {
                if (words[0].length >= MAXLEN) throw Exception("YAGDFF: Name too long")
            }

            var faceVertexCounter = 3
            var faceColorCounter = 3
            var i = 0

            while (true) {
                val word = words[i]

                if (i == word.length - 1 && word != "endyagdff") {
                    LOGGER.warn("YAGDFF: Unexpected EOF. 'endyagdff' keyword was expected")
                    break
                }

                if (word == "face") {
                    if (faceVertexCounter != 3) LOGGER.warn("YAGDFF: Began new face but old face has not been completed")

                    faceVertexCounter = 0
                    faceColorCounter = 0
                    val vn = Vec3f(0f, 0f, 0f)

                    val next = words[i + 1]
                    if (next == "normal") LOGGER.warn("YAGDFF: Face normal was expected but not found")
                    else {
                        try {
                            i++
                            vn.x = words[++i].f
                            vn.y = words[++i].f
                            vn.z = words[++i].f
                            normalBuffer.add(vn)
                            normalBuffer.add(vn.copy())
                            normalBuffer.add(vn.copy())
                        } catch (e: NumberFormatException) {
                            throw Exception("YAGDFF: Unexpected EOF while parsing face")
                        }
                    }
                } else if (word == "vertex") {
                    if (faceVertexCounter >= 3) {
                        LOGGER.warn("YAGDFF: Face with more than 3 vertices was found")
                        i++
                    } else {
                        try {
                            val vn = Vec3f(0f, 0f, 0f)
                            vn.x = words[++i].f
                            vn.y = words[++i].f
                            vn.z = words[++i].f
                            vertexBuffer.add(vn)
                            faceVertexCounter++
                        } catch (e: NumberFormatException) {
                            throw Exception("YAGDFF: Unexpected EOF while parsing face")
                        }
                    }
                } else if (word == "color") {
                    if (faceColorCounter >= 3) {
                        LOGGER.warn("YAGDFF: Face with more than 3 colors was found")
                        i++
                    } else if (faceColorCounter == 1 && words[i + 5] != "color") {
                        try {
                            val vn = Vec4d(0.0, 0.0, 0.0, 0.0)
                            vn.x = words[++i].d
                            vn.y = words[++i].d
                            vn.z = words[++i].d
                            vn.w = words[++i].d
                            colorBuffer.add(vn)
                            colorBuffer.add(vn.copy())
                            faceColorCounter += 2
                        } catch (e: NumberFormatException) {
                            throw Exception("YAGDFF: Unexpected EOF while parsing face")
                        }
                    } else if (faceColorCounter == 0 && words[i + 5] != "color") {
                        try {
                            val vn = Vec4d(0.0, 0.0, 0.0, 0.0)
                            vn.x = words[++i].d
                            vn.y = words[++i].d
                            vn.z = words[++i].d
                            vn.w = words[++i].d
                            colorBuffer.add(vn)
                            colorBuffer.add(vn.copy())
                            colorBuffer.add(vn.copy())
                            faceColorCounter += 3
                        } catch (e: NumberFormatException) {
                            throw Exception("YAGDFF: Unexpected EOF while parsing face")
                        }
                    } else {
                        try {
                            val vn = Vec4d(0.0, 0.0, 0.0, 0.0)
                            vn.x = words[++i].d
                            vn.y = words[++i].d
                            vn.z = words[++i].d
                            vn.w = words[++i].d
                            colorBuffer.add(vn)
                            faceColorCounter++
                        } catch (e: NumberFormatException) {
                            throw Exception("YAGDFF: Unexpected EOF while parsing face")
                        }
                    }
                } else if (word == "endyagdff" || word == "endsolid") break

                i++
            }

            if (vertexBuffer.isEmpty())
                throw Exception("YAGDFF: File is empty or invalid; no data loaded")
            if (vertexBuffer.size % 3 != 0)
                throw Exception("YAGDFF: Invalid number of vertices")
            if (colorBuffer.size % 3 != 0)
                throw Exception("YAGDFF: Invalid number of colors")
            if (normalBuffer.size != vertexBuffer.size)
                throw Exception("YAGDFF: Normal buffer size does not match vertex buffer size")
            if (colorBuffer.size != vertexBuffer.size)
                throw Exception("YAGDFF: Color buffer size does not match vertex buffer size")

            val normalPartition = ListPartition.ofSize(normalBuffer, 3)
            val vertexPartition = ListPartition.ofSize(vertexBuffer, 3)
            val colorPartition = ListPartition.ofSize(colorBuffer, 3)

            for (f in 0 until vertexPartition.size) {
                val face = Face(normalPartition[f], vertexPartition[f], colorPartition[f])
                faceBuffer.add(face)
            }

            return Geometry(Geometry.Type.TRIANGLES, faceBuffer)
        }

        private val String.words: List<String>
            get() = trim().split("\\s+".toRegex())
    }

    override fun canLoad(id: Identifier, manager: ResourceManager): Boolean {
        return isYAGDFF(getResourceByteBuffer(id, manager))
    }

    override fun load(id: Identifier, manager: ResourceManager): Geometry? = loadYAGDFF(getResourceByteBuffer(id, manager))
}