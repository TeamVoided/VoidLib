package org.teamvoided.voidlib.vui

import net.minecraft.client.util.math.MatrixStack
import org.teamvoided.voidlib.core.datastructures.Vec2i

data class DrawContext(val matrices: MatrixStack, val mousePos: Vec2i, val partialTicks: Float, val delta: Float, val drawTooltip: Boolean)