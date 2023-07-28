package org.teamvoided.voidlib.woodset.sign.block

import net.minecraft.block.WallSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.woodset.sign.VoidSign

class VoidWallSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    WallSignBlock(settings, woodType), VoidSign

