package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.text.Text
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2d
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.vui.v2.animation.EasingFunction
import org.teamvoided.voidlib.vui.v2.animation.Interpolator
import org.teamvoided.voidlib.vui.v2.node.*
import org.teamvoided.voidlib.vui.v2.screen.VoidUIAdapter
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen
import kotlin.random.Random

class EditorScreen: VuiScreen<BoxNode>(Text.literal("Vui Editor")) {
    override val uiAdapter = VoidUIAdapter.create(this) { pos, size -> BoxNode(pos, size, ARGB(255, 0, 0, 0)) }

    val boxNode = BoxNode(Vec2i(0,0), Vec2i(100, 100), ARGB(255, 0, 100, 200))
/*    val animatedNode = AnimatedNode(
//        boxNode
//    ) { node ->
//        node as BoxNode
//        listOf(
//            Animation(
//                5,
//                Animation.Direction.Forwards,
//                true,
//                InterpolatedProperty(node.topLeftColor, Interpolator.colorInterpolator) { node.topLeftColor = it },
//                EasingFunction.sine,
//                ARGB(255, 200, 200, 200)
//            ),
//            Animation(
//                5,
//                Animation.Direction.Forwards,
//                true,
//                InterpolatedProperty(node.topRightColor, Interpolator.colorInterpolator) { node.topRightColor = it },
//                EasingFunction.exponential,
//                ARGB(255, 240, 200, 200)
//            ),
//            Animation(
//                5,
//                Animation.Direction.Forwards,
//                true,
//                InterpolatedProperty(node.bottomLeftColor, Interpolator.colorInterpolator) { node.bottomLeftColor = it },
//                EasingFunction.quartic,
//                ARGB(255, 210, 180, 80)
//            ),
//            Animation(
//                5,
//                Animation.Direction.Forwards,
//                true,
//                InterpolatedProperty(node.bottomRightColor, Interpolator.colorInterpolator) { node.bottomRightColor = it },
//                EasingFunction.linear,
//                ARGB(255, 100, 200, 40)
//            )
//        )
//    }
 */

    val button = ButtonNode(Vec2i(100, 100), Vec2i(200, 50), Text.literal("A Button"))

    val colorSelector = ColorPickerNode(Vec2i(100, 175), Vec2i(400, 200))

    val slider = SliderNode(Vec2i(100, 400), Vec2i(200, 25))

    val mov2 = MovableNode(boxNode)

    val subAdapter = VoidUIAdapter.create(
        this,
        Vec2i(500, 500), Vec2i(300, 300),
        { pos, size -> BoxNode(pos, size, ARGB(0, 0, 0, 0)) },
        debugBox = true
    )

    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here
        button.buttonPressCallback += {
            val delta = Random.nextDouble(0.0, 1.0)
            val easing = EasingFunction.sine
            val mousePos = Vec2d(
                Random.nextDouble(colorSelector.pos.x.d, colorSelector.pos.x + colorSelector.size.x.d),
                Random.nextDouble(colorSelector.pos.y.d, colorSelector.pos.y + colorSelector.size.y.d)
            )

            val mousePos2 = Vec2d(
                Random.nextDouble(colorSelector.pos.x.d, colorSelector.pos.x + colorSelector.size.x.d),
                Random.nextDouble(colorSelector.pos.y.d, colorSelector.pos.y + colorSelector.size.y.d)
            )

            val interpolated = Interpolator.vec2dInterpolator(mousePos, mousePos2, delta.f, easing)

            colorSelector.updateFromMouse(interpolated)
        }

        uiAdapter.drawCallback

        root.addChild(mov2)
        root.addChild(button)
        colorSelector.showAlpha = true
        root.addChild(colorSelector)
        root.addChild(slider)

        subAdapter.rootNode.addChild(MovableNode(BoxNode(Vec2i(0, 0), Vec2i(50, 50), ARGB(255, 255, 255))))
    }

    override fun vuiUpdate() {
        boxNode.topLeftColor = colorSelector.color
        boxNode.topRightColor = colorSelector.color
        boxNode.bottomLeftColor = colorSelector.color
        boxNode.bottomRightColor = colorSelector.color
    }
}