package org.teamvoided.voidlib.woodset.sign.block

import net.minecraft.block.SignBlock
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.teamvoided.voidlib.woodset.sign.VoidSign

class VoidSignBlock(override val texture: Identifier, settings: Settings, woodType: WoodType) :
    SignBlock(settings, woodType), VoidSign

