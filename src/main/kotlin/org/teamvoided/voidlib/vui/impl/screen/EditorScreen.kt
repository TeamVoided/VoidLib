package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.animation.Animation
import org.teamvoided.voidlib.vui.v2.animation.EasingFunction
import org.teamvoided.voidlib.vui.v2.animation.InterpolatedProperty
import org.teamvoided.voidlib.vui.v2.animation.Interpolator
import org.teamvoided.voidlib.vui.v2.node.*
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen

class EditorScreen : VuiScreen(Text.literal("Vui Editor")) {
    override val parent = ParentNode()

    val bg = BoxNode(Vec2i(0,0), Vec2i(1000, 1000), ARGB(255u, 0u, 0u, 0u))

    val boxNode = BoxNode(Vec2i(0,0), Vec2i(100, 100), ARGB(255u, 0u, 100u, 200u))
    val animatedNode = AnimatedNode(
        boxNode
    ) { node ->
        node as BoxNode
        listOf(
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.topLeftColor, Interpolator.colorInterpolator) { node.topLeftColor = it },
                EasingFunction.sine,
                ARGB(255u, 200u, 200u, 200u)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.topRightColor, Interpolator.colorInterpolator) { node.topRightColor = it },
                EasingFunction.exponential,
                ARGB(255u, 240u, 200u, 200u)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.bottomLeftColor, Interpolator.colorInterpolator) { node.bottomLeftColor = it },
                EasingFunction.quartic,
                ARGB(255u, 210u, 180u, 80u)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.bottomRightColor, Interpolator.colorInterpolator) { node.bottomRightColor = it },
                EasingFunction.linear,
                ARGB(255u, 100u, 200u, 40u)
            )
        )
    }

    val mov2 = MovableNode(animatedNode)

    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here

        parent.addChild(bg)
        bg.addChild(mov2)
    }

    override fun shouldPause(): Boolean = false
}