package org.teamvoided.voidlib.woodset.sign.block

import net.minecraft.block.HangingSignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.woodset.sign.VoidSign

class VoidHangingSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    HangingSignBlock(settings, woodType), VoidSign

