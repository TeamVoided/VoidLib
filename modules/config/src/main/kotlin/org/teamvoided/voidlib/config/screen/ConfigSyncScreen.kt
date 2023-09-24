package org.teamvoided.voidlib.config.screen

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.config.ConfigManager
import org.teamvoided.voidlib.config.VoidFigHelpers
import org.teamvoided.voidlib.config.impl.VoidFigImpl
import org.teamvoided.voidlib.core.ARGB
import org.teamvoided.voidlib.core.datastructures.vector.Vec2i
import org.teamvoided.voidlib.vui.v2.node.BoxNode
import org.teamvoided.voidlib.vui.v2.node.ButtonNode
import org.teamvoided.voidlib.vui.v2.screen.VoidUIAdapter
import org.teamvoided.voidlib.vui.v2.screen.VuiScreen
import kotlin.system.exitProcess

class ConfigSyncScreen(val mismatched: Map<Identifier, String>): VuiScreen<BoxNode>(Text.literal("Config Sync Screen")) {
    override val uiAdapter: VoidUIAdapter<BoxNode> = VoidUIAdapter.create(this) { pos, size ->
        BoxNode(pos, size, ARGB(0, 0, 0))
    }

    val syncAndRestart = ButtonNode(Vec2i(width / 2 - 40, height / 2 + 10), Vec2i(80, 20), Text.literal("Sync Configs and Restart"))
    val serverList = ButtonNode(Vec2i(width / 2 - 40, height / 2 - 10), Vec2i(80, 20), Text.literal("Back to Server List"))

    override fun vuiInit() {
        root.addChild(syncAndRestart)
        root.addChild(serverList)

        syncAndRestart.buttonPressCallback += {
            mismatched.forEach { (id, rawData) ->
                VoidFigImpl.saveCommonConfigsOnClient = false
                VoidFigHelpers.writeConfigData(ConfigManager.commonConfigs[id]!!, rawData)
                exitProcess(0)
            }
        }

        serverList.buttonPressCallback += {
            val client = MinecraftClient.getInstance()
            client.setScreen(MultiplayerScreen(TitleScreen(true)))
        }
    }

    override fun shouldCloseOnEsc(): Boolean = false
}