package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.v2.animation.Animation
import org.teamvoided.voidlib.vui.v2.animation.EasingFunction
import org.teamvoided.voidlib.vui.v2.animation.InterpolatedProperty
import org.teamvoided.voidlib.vui.v2.animation.Interpolator
import org.teamvoided.voidlib.vui.v2.node.*
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen

class EditorScreen : VuiScreen(Text.literal("Vui Editor")) {
    override val parent = ParentNode()

    val bg = BoxNode(Vec2i(0,0), Vec2i(1000, 1000), ARGB(255, 0, 0, 0))

    val boxNode = BoxNode(Vec2i(0,0), Vec2i(100, 100), ARGB(255, 0, 100, 200))
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
                ARGB(255, 200, 200, 200)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.topRightColor, Interpolator.colorInterpolator) { node.topRightColor = it },
                EasingFunction.exponential,
                ARGB(255, 240, 200, 200)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.bottomLeftColor, Interpolator.colorInterpolator) { node.bottomLeftColor = it },
                EasingFunction.quartic,
                ARGB(255, 210, 180, 80)
            ),
            Animation(
                5,
                Animation.Direction.Forwards,
                true,
                InterpolatedProperty(node.bottomRightColor, Interpolator.colorInterpolator) { node.bottomRightColor = it },
                EasingFunction.linear,
                ARGB(255, 100, 200, 40)
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