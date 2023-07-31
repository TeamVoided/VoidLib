package org.teamvoided.voidlib.woodset.sign.block

import net.minecraft.block.WallHangingSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.woodset.sign.VoidSign

class VoidWallHangingSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    WallHangingSignBlock(settings, woodType), VoidSign

