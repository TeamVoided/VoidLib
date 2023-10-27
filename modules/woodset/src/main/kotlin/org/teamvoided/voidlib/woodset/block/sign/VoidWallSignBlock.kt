package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.WallSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier

class VoidWallSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    WallSignBlock(settings.solid(), woodType), VoidSign

