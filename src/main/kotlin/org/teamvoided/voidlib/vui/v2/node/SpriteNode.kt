package org.teamvoided.voidlib.vui.v2.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.texture.Sprite
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.core.datastructures.Vec3i
import org.teamvoided.voidlib.vui.v2.event.Event

open class SpriteNode(val sprite: () -> Sprite) : Node() {
    private var z: Int = 0

    constructor(pos: Vec3i, size: Vec2i, sprite: () -> Sprite) : this(sprite) {
        this.pos = Vec2i(pos.x, pos.y)
        this.size = size
        z = pos.z
    }

    constructor(pos: Vec3i, sprite: () -> Sprite) : this(sprite) {
        this.pos = Vec2i(pos.x, pos.y)
        z = pos.z
    }

    override fun draw(event: Event.LogicalEvent.DrawEvent) {
        event.ensurePreChild {
            val sprite = this.sprite()
            RenderSystem.setShaderTexture(0, sprite.atlasId)
            DrawableHelper.drawSprite(event.drawContext.matrices, globalPos.x, globalPos.y, z, size.x, size.y, sprite)
        }
    }


}