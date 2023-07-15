package org.teamvoided.voidlib.vui.v2.rendering.scissor

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.opengl.GL11
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.datastructures.vector.Vec4f
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.core.i
import org.teamvoided.voidlib.vui.v2.node.Node
import java.util.*
import java.util.function.Consumer
import kotlin.math.ceil

object ScissorStack {
    private val emptyMatrixStack = MatrixStack()
    private val stack: Deque<ScissorBox> = ArrayDeque()

    fun pushDirect(scissorBox: ScissorBox) {
        val window = MinecraftClient.getInstance().window
        val scale = window.scaleFactor
        push(
            scissorBox.withScale(scale, window.scaledHeight, ScissorBox.ScaleMethod.DIVISION),
            emptyMatrixStack
        )
    }

    fun push(scissorBox: ScissorBox, matrices: MatrixStack) {
        val newFrame = applyGlTransform(scissorBox, matrices)
        if (stack.isEmpty()) {
            stack.push(newFrame)
        } else {
            val top = stack.peek()
            stack.push(top.intersection(newFrame))
        }
        applyState()
    }

    fun pop() {
        if (!stack.isEmpty()) {
            stack.pop()
            applyState()
        }
    }

    private fun applyState() {
        if (stack.isEmpty()) {
            val window = MinecraftClient.getInstance().window
            GL11.glScissor(0, 0, window.framebufferWidth, window.framebufferHeight)
            return
        }

        if (!GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)) return
        val newFrame = stack.peek()
        val window = MinecraftClient.getInstance().window
        val scale = window.scaleFactor
        newFrame.start(scale, window.framebufferHeight)
    }

    fun drawUnclipped(action: () -> Unit) {
        val scissorEnabled: Boolean = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)
        if (scissorEnabled) GlStateManager._disableScissorTest()
        action()
        if (scissorEnabled) GlStateManager._enableScissorTest()
    }

    fun popFramesAndDraw(maxPopFrames: Int, action: () -> Unit) {
        var maxPopFrames2 = maxPopFrames
        val previousFrames: ArrayList<ScissorBox> = ArrayList<ScissorBox>()
        while (maxPopFrames2 > 1 && stack.size > 1) {
            previousFrames.add(0, stack.pop())
            maxPopFrames2--
        }
        applyState()
        action()
        previousFrames.forEach(Consumer { e: ScissorBox -> stack.push(e) })
        applyState()
    }

    fun isVisible(pos: Vec2i, matrices: MatrixStack): Boolean {
        val top: ScissorBox = stack.peek() ?: return true
        return top.intersects(applyGlTransform(SimpleScissorBox(pos, Vec2i(0, 0)), matrices))
    }

    fun isVisible(node: Node, matrices: MatrixStack): Boolean {
        val top: ScissorBox = stack.peek() ?: return true
        return top.intersects(applyGlTransform(SimpleScissorBox(node.pos, node.size), matrices))
    }

    private fun applyGlTransform(scissorBox: ScissorBox, matrices: MatrixStack): ScissorBox {
        matrices.push()
        matrices.multiplyPositionMatrix(RenderSystem.getModelViewMatrix())
        var root = Vec4f(scissorBox.pos.x.f, scissorBox.pos.y.f, 0f, 1f)
        var end = Vec4f((scissorBox.pos.x + scissorBox.size.x).f, (scissorBox.pos.y + scissorBox.size.y).f, 0f, 1f)
        root *= (matrices.peek().positionMatrix)
        end *= (matrices.peek().positionMatrix)
        scissorBox.pos.x = root.x.i
        scissorBox.pos.y = root.y.i
        scissorBox.size.x = ceil((end.x - root.x).d).i
        scissorBox.size.y = ceil((end.y - root.y).d).i
        matrices.pop()
        return scissorBox
    }
}
