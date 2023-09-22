package org.teamvoided.voidlib.vui.impl.screen

import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.LOGGER
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

class EditorScreen : VuiScreen<BoxNode>(Text.literal("Vui Editor")) {
    override val uiAdapter = VoidUIAdapter.create(this) { pos, size -> BoxNode(pos, size, ARGB(255, 0, 0, 0)) }

    val boxNode = BoxNode(Vec2i(0, 0), Vec2i(100, 100), ARGB(255, 0, 100, 200))
    /*    val animatedNode = AnimatedNode(
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
     */

    val button = ButtonNode(Vec2i(100, 100), Vec2i(200, 50), Text.literal("A Button"))

    val colorSelector = ColorPickerNode(Vec2i(100, 175), Vec2i(400, 200))

    val slider = SliderNode(Vec2i(100, 400), Vec2i(200, 25))

    val mov2 = MovableNode(boxNode)

    val container = ContainerNode(Vec2i(500, 500), Vec2i(500, 500))

    val textNode = TextNode(Vec2i(400, 400), Vec2i(0, 1), Text.of("something"), ARGB(255, 255, 255), this)
    val movNode = MovableNode(BoxNode(Vec2i(0, 0), Vec2i(50, 50), ARGB(255, 255, 255)))

    val clickableLink = TextNode(
        Vec2i(100, 25), Vec2i(2, 2),
        Text.literal("heyo ").formatted(Formatting.RED).append(
            Text.literal("Click here!").styled {
                it.withColor(Formatting.GREEN)
                    .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com"))
                    .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to link")))
            }
        ),
        this
    )

    val demoTextNode =
        TextNode(Vec2i(200, 50), Vec2i(0, 1), Text.literal("RENDER THIS").formatted(Formatting.WHITE), this)


    val yesM = BoxNode(Vec2i(350, 50), Vec2i(100, 100), ARGB( 50, 50, 50))
    val textBox = TextInputNode(Vec2i(350, 50), Vec2i(100, 100), ARGB.WHITE)

    val widgetNode = WidgetNode(Vec2i(250, 50), Vec2i(40, 40)) { pos: Vec2i, size: Vec2i ->
        ButtonWidget.builder(Text.literal("pain")) { LOGGER.info("hello") }
            .position(pos.x, pos.y).size(size.x, size.y).build()
    }


    override fun vuiInit() {
        //if there are no nodes the window will never flush out the minecraft loading screen cuz there's nothing to render on top of it
        //Add nodes here

        textBox.onChangeCallback += {
            LOGGER.info("chenge ${it.text}")
            demoTextNode.text = Text.literal(it.text)
        }

        button.buttonPressCallback += {
            LOGGER.info("---")
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

        root.addChild(container)

        root.addChild(textNode)
        root.addChild(clickableLink)

        root.addChild(widgetNode)

        root.addChild(yesM)
        yesM.addChild(textBox)
        root.addChild(demoTextNode)


        container.debugBox = true
        container.addChild(movNode)
        container.updateCallback += {
            textNode.text = Text.literal("Pos: ${container.children()[0].pos} | Size: ${container.children()[0].size}")
        }
    }

    override fun vuiUpdate() {
        boxNode.topLeftColor = colorSelector.color
        boxNode.topRightColor = colorSelector.color
        boxNode.bottomLeftColor = colorSelector.color
        boxNode.bottomRightColor = colorSelector.color
    }
}