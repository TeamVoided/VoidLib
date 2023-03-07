package org.teamvoided.voidlib.vui.node

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.texture.Sprite
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.core.datastructures.Vec2i
import org.teamvoided.voidlib.vui.DrawContext
import org.teamvoided.voidlib.vui.node.NodeIds.SPRITE

open class SpriteNode(val sprite: () -> Sprite, override var name: String): Node() {
    override var drawCallback: ((context: DrawContext) -> Unit)? = { context ->
        val sprite = this.sprite()
        RenderSystem.setShaderTexture(0, sprite.atlasId)
        DrawableHelper.drawSprite(context.matrices, globalPos.x, globalPos.y, 0, size.x, size.y, sprite)
    }

    constructor(pos: Vec2i, sprite: () -> Sprite, name: String): this(sprite, name) {
        this.pos = pos
    }

    constructor(pos: Vec2i, size: Vec2i, sprite: () -> Sprite, name: String): this(sprite, name) {
        this.pos = pos
        this.size = size
    }


    override fun typeId(): Identifier = SPRITE
}