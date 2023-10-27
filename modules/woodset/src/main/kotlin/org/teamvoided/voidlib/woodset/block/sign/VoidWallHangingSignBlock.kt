package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.WallHangingSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier

class VoidWallHangingSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    WallHangingSignBlock(settings.solid(), woodType), VoidSign

