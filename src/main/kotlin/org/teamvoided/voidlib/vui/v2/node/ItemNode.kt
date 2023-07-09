package org.teamvoided.voidlib.vui.v2.node

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import org.joml.Matrix4f
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
import java.util.*

open class ItemNode(var stack: ItemStack): Node() {
    protected val entityBuffers: VertexConsumerProvider.Immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
    protected val itemRenderer: ItemRenderer = MinecraftClient.getInstance().itemRenderer
    var showOverlay = false
    var showTooltip = false

    companion object {
        protected val itemScaling: Matrix4f = Matrix4f().scaling(16f, -16f, 16f)

        fun tooltipFromItem(
            stack: ItemStack,
            player: PlayerEntity?,
            context: TooltipContext?
        ): List<TooltipComponent> {
            var context = context
            if (context == null) {
                context = if (MinecraftClient.getInstance().options.advancedItemTooltips) TooltipContext.ADVANCED else TooltipContext.BASIC
            }

            val tooltip = ArrayList<TooltipComponent>()
            stack.getTooltip(player, context)
                .stream()
                .map { obj: Text -> obj.asOrderedText() }
                .map { text: OrderedText -> TooltipComponent.of(text) }
                .forEach { e: TooltipComponent -> tooltip.add(e) }

            stack.tooltipData.ifPresent { data: TooltipData ->
                Objects.requireNonNullElseGet(
                    TooltipComponentCallback.EVENT.invoker().getComponent(data)
                ) { TooltipComponent.of(data) }?.let { tooltip.add(1, it) }
            }

            return tooltip
        }
    }

    constructor(pos: Vec2i, stack: ItemStack): this(stack) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, stack: ItemStack): this(stack) {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val matrices = event.drawContext.matrices

            val notSideLit = !this.itemRenderer.getModel(stack, null, null, 0).isSideLit
            if (notSideLit) {
                DiffuseLighting.disableGuiDepthLighting()
            }

            matrices.push()

            matrices.translate(globalPos.x.f, globalPos.y.f, 100f)

            matrices.scale(size.x / 16f, size.y / 16f, 1f)
            matrices.translate(8.0, 8.0, 0.0)

            if (notSideLit) {
                matrices.scale(16f, -16f, 16f)
            } else {
                matrices.multiplyPositionMatrix(itemScaling)
            }

            itemRenderer.renderItem(
                stack,
                ModelTransformationMode.GUI,
                LightmapTextureManager.MAX_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV,
                matrices,
                entityBuffers,
                null,
                0
            )

            entityBuffers.draw()

            matrices.pop()

            if (showOverlay) {
                itemRenderer.renderGuiItemOverlay(
                    matrices, MinecraftClient.getInstance().textRenderer,
                    stack, globalPos.x, globalPos.y
                )
            }

            val parent = parent()

            if (showTooltip && ((parent != null && parent.childAt(event.drawContext.mousePos) == this) || (parent == null && isTouching(event.drawContext.mousePos)))) {
                Pencil.drawToolTip(matrices, globalPos, tooltipFromItem(stack, MinecraftClient.getInstance().player, null))
            }

            if (notSideLit) {
                DiffuseLighting.enableGuiDepthLighting()
            }
        }
    }
}