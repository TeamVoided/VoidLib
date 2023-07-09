package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.RotationAxis
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.extension.EntityRendererDispatcherExt.Companion.counterRotate
import org.teamvoided.voidlib.vui.v2.extension.EntityRendererDispatcherExt.Companion.showNametag
import kotlin.math.atan
import kotlin.math.min

open class EntityNode(val entity: Entity): Node() {
    protected val dispatcher: EntityRenderDispatcher
    protected val entityBuffers: VertexConsumerProvider.Immediate

    protected var mouseRotation = 0f
    var scale = 1f
    var lookAtCursor = false
    var allowMouseRotation = false
    var scaleToFit = false
        set(value) {
            field = value
            if (value) {
                val xScale = .5f / entity.width
                val yScale = .5f / entity.height

                scale = min(xScale, yScale)
            }
        }

    var showNametag = false
    var transform: (stack: MatrixStack) -> Unit = { }

    init {
        val client = MinecraftClient.getInstance()
        dispatcher = client.entityRenderDispatcher
        entityBuffers = client.bufferBuilders.entityVertexConsumers
    }

    constructor(pos: Vec2i, entity: Entity): this(entity) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, entity: Entity): this(entity) {
        this.pos = pos
        this.size = size
    }

    constructor(pos: Vec2i, entityType: EntityType<*>): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos

        val client = MinecraftClient.getInstance()
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)

    }

    constructor(pos: Vec2i, size: Vec2i, entityType: EntityType<*>): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos
        this.size = size

        val client = MinecraftClient.getInstance()
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)
    }

    constructor(pos: Vec2i, entityType: EntityType<*>, nbtMutator: NbtCompound.() -> Unit): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos

        val client = MinecraftClient.getInstance()
        val nbt = NbtCompound()
        nbtMutator(nbt)
        entity.readNbt(nbt)
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)

    }

    constructor(pos: Vec2i, size: Vec2i, entityType: EntityType<*>, nbtMutator: NbtCompound.() -> Unit): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos
        this.size = size

        val client = MinecraftClient.getInstance()
        val nbt = NbtCompound()
        nbtMutator(nbt)
        entity.readNbt(nbt)
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)
    }

    constructor(pos: Vec2i, entityType: EntityType<*>, nbt: NbtCompound): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos

        val client = MinecraftClient.getInstance()
        entity.readNbt(nbt)
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)

    }

    constructor(pos: Vec2i, size: Vec2i, entityType: EntityType<*>, nbt: NbtCompound): this(
        entityType.create(MinecraftClient.getInstance().world) ?: throw IllegalStateException("Failed to create entity")
    ) {
        this.pos = pos
        this.size = size

        val client = MinecraftClient.getInstance()
        entity.readNbt(nbt)
        entity.updatePosition(client.player!!.x, client.player!!.y, client.player!!.z)
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val matrices = event.drawContext.matrices
            val mousePos = event.drawContext.mousePos
            matrices.push()

            matrices.translate(globalPos.x + size.x / 2f, globalPos.y + size.y /2f, 100f)
            matrices.scale(75 * scale * size.x /64f, -75 * scale * size.y / 64f, 75 * scale)
            matrices.translate(0f, entity.height / -2f, 0f)

            transform(matrices)

            if (lookAtCursor) {
                var xRotation = Math.toDegrees(atan((mousePos.y - globalPos.y - size.y / 2.0) / 40f)).f
                val yRotation = Math.toDegrees(atan((mousePos.x - globalPos.x - size.x / 2.0) / 40f)).f
                if (entity is LivingEntity) {
                    entity.prevHeadYaw = -yRotation
                }
                entity.prevYaw = -yRotation
                entity.prevPitch = xRotation * .65f

                if (xRotation == 0f) xRotation = .1f
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(xRotation * .15f))
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yRotation * .15f))
            } else {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35f))
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45 + mouseRotation))
            }

            dispatcher.showNametag = showNametag
            dispatcher.counterRotate = true

            RenderSystem.setShaderLights(Vector3f(.15f, 1f, 0f), Vector3f(.15f, -1f, 0f))
            dispatcher.setRenderShadows(false)
            dispatcher.render(entity, 0.0, 0.0, 0.0, 0f, 0f, matrices, entityBuffers, LightmapTextureManager.MAX_LIGHT_COORDINATE)
            dispatcher.setRenderShadows(true)
            entityBuffers.draw()
            DiffuseLighting.enableGuiDepthLighting()

            matrices.pop()

            dispatcher.showNametag = true
            dispatcher.counterRotate = false
        }
    }

    override fun onMouseDrag(event: Event.InputEvent.MouseDragEvent) {
        if (allowMouseRotation && event.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            mouseRotation += event.delta.x.f
            event.cancel()
        }
    }
}
