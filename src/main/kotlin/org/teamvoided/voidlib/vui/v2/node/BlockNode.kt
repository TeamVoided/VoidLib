package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.util.math.RotationAxis
import org.joml.Vector3f
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.event.Event.LogicalEvent.DrawEvent


class BlockNode(val state: BlockState, val entity: BlockEntity? = null): Node() {
    private val client = MinecraftClient.getInstance()

    constructor(state: BlockState, entity: BlockEntity?, pos: Vec2i): this(state, entity) {
        this.pos = pos
    }

    constructor(state: BlockState, entity: BlockEntity?, pos: Vec2i, size: Vec2i): this(state, entity) {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: DrawEvent) {
        event.ensurePreChild {
            val matrices = event.drawContext.matrices

            matrices.push()
            matrices.translate(globalPos.x + size.x / 2.0, globalPos.y + size.y / 2.0, 100.0)
            matrices.scale(40 * size.x / 64f, -40 * size.y / 64f, 40f)

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f))
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(225f))

            matrices.translate(-.5, -.5, -.5)

            //Temporarily sets graphics quality to fancy to render block
            RenderSystem.runAsFancy {
                val vertexConsumers = client.bufferBuilders.entityVertexConsumers
                if (state.renderType !== BlockRenderType.ENTITYBLOCK_ANIMATED) {
                    this.client.blockRenderManager.renderBlockAsEntity(
                        this.state, matrices, vertexConsumers,
                        LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV
                    )
                }
                if (this.entity != null) {
                    client.blockEntityRenderDispatcher.get(entity)?.render(
                        entity,
                        event.drawContext.partialTicks,
                        matrices,
                        vertexConsumers,
                        LightmapTextureManager.MAX_LIGHT_COORDINATE,
                        OverlayTexture.DEFAULT_UV
                    )
                }
                RenderSystem.setShaderLights(Vector3f(-1.5f, -.5f, 0f), Vector3f(0f, -1f, 0f))
                vertexConsumers.draw()
                DiffuseLighting.enableGuiDepthLighting()
            }

            matrices.pop()
        }
    }
}