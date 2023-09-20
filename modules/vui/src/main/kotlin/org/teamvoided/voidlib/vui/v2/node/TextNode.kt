package org.teamvoided.voidlib.vui.v2.node

import com.google.common.collect.Sets
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.ClickEvent
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Util
import org.slf4j.LoggerFactory
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.core.f
import org.teamvoided.voidlib.vui.v2.event.ui.Event
import org.teamvoided.voidlib.vui.v2.rendering.Pencil
import java.io.File
import java.net.URI
import java.net.URISyntaxException

open class TextNode(var text: Text, var textColor: ARGB, val screen: Screen) : Node() {
    private val allowedProtocols: Set<String> = Sets.newHashSet(*arrayOf("http", "https"))
    private val LOGGER = LoggerFactory.getLogger("TextNode")
    private var clickedLink: URI? = null

    constructor(pos: Vec2i, text: Text, textColor: ARGB, screen: Screen) : this(text, textColor, screen) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, text: Text, textColor: ARGB, screen: Screen) : this(text, textColor, screen) {
        this.pos = pos
        this.size = size
    }

    constructor(pos: Vec2i, size: Vec2i, text: Text, screen: Screen) : this(text, ARGB.WHITE, screen) {
        this.pos = pos
        this.size = size
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        Pencil.drawText(event.drawContext, text, globalPos, size.y.f, textColor)
    }

    override fun onMousePress(event: Event.InputEvent.MousePressEvent) {
        if (this.isTouching(event.pos)) {
            text.siblings.forEach { handleTextClick(it.style) }
        }
    }

    open fun handleTextClick(style: Style): Boolean {
        val client = MinecraftClient.getInstance()

        val clickEvent = style.clickEvent
        if (clickEvent != null) {
            val value = clickEvent.value
            when (clickEvent.action) {
                ClickEvent.Action.OPEN_URL -> {
                    if (!client.options.chatLinks.value) return@handleTextClick false
                    try {
                        val uRI = URI(value)
                        val string = uRI.scheme ?: throw URISyntaxException(value, "Missing protocol")
                        if (!allowedProtocols.contains(string.lowercase()))
                            throw URISyntaxException(value, "Unsupported protocol: " + string.lowercase())
                        if (client.options.chatLinksPrompt.value) {
                            clickedLink = uRI
                            client.setScreen(ConfirmLinkScreen({ confirmLink(it) }, value, false))
                        } else openLink(uRI)

                    } catch (var5: URISyntaxException) {
                        LOGGER.error("Can't open url for {}", clickEvent, var5)
                    }
                }

                ClickEvent.Action.OPEN_FILE -> openLink(File(value).toURI())
                ClickEvent.Action.COPY_TO_CLIPBOARD -> client.keyboard.clipboard = value
                else -> {
                    LOGGER.error("Don't know how to handle {}", clickEvent)
                }
            }
            return true
        }
        return false

    }

    private fun openLink(uRI: URI) = Util.getOperatingSystem().open(uRI)

    private fun confirmLink(bl: Boolean) {
        if (bl) openLink(clickedLink!!)
        clickedLink = null
        MinecraftClient.getInstance().setScreen(screen)
    }


}