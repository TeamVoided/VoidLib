package org.teamvoided.voidlib.woodset.block.sign

import net.minecraft.block.HangingSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier

class VoidHangingSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    HangingSignBlock(settings.solid(), woodType), VoidSign

