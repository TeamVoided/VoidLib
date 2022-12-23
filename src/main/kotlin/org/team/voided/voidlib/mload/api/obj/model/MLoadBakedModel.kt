package org.team.voided.voidlib.mload.api.obj.model

import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.BlockRenderView
import org.team.voided.voidlib.mload.MLoadImpl
import java.util.LinkedList
import java.util.function.Supplier


class MLoadBakedModel(
    private val mesh: Mesh?,
    private val transformation: ModelTransformation,
    private val sprite: Sprite,
    private val isSideLit: Boolean) : BakedModel, FabricBakedModel {

    private var backupQuads: MutableList<BakedQuad>? = null

    override fun emitBlockQuads(
        blockRenderView: BlockRenderView,
        blockState: BlockState,
        blockPos: BlockPos,
        randomSupplier: Supplier<Random>,
        context: RenderContext
    ) {
        if (mesh != null) {
            context.meshConsumer().accept(mesh)
        } else {
            MLoadImpl.LOGGER.warn("Mesh is null while emitting block quads for block {}", blockState.block.name.string)
        }
    }

    override fun emitItemQuads(itemStack: ItemStack, supplier: Supplier<Random?>?, renderContext: RenderContext) {
        if (mesh != null) {
            renderContext.meshConsumer().accept(mesh)
        } else {
            MLoadImpl.LOGGER.warn("Mesh is null while emitting block quads for item {}", itemStack.item.name.string)
        }
    }

    override fun getQuads(
        state: BlockState?,
        face: Direction?,
        random: Random
    ): List<BakedQuad?>? {
        if (this.backupQuads == null) {
            this.backupQuads = LinkedList()
            mesh?.forEach { quadView: QuadView ->
                this.backupQuads?.add(
                    quadView.toBakedQuad(0, sprite, false)
                )
            }
        }
        return this.backupQuads
    }

    override fun useAmbientOcclusion(): Boolean {
        return true
    }

    override fun hasDepth(): Boolean {
        return false
    }

    override fun isSideLit(): Boolean {
        return isSideLit
    }

    override fun isBuiltin(): Boolean {
        return false
    }

    override fun getParticleSprite(): Sprite {
        return sprite
    }

    override fun getTransformation(): ModelTransformation {
        return this.transformation
    }

    override fun getOverrides(): ModelOverrideList? {
        return ModelOverrideList.EMPTY
    }

    override fun isVanillaAdapter(): Boolean {
        return false
    }
}